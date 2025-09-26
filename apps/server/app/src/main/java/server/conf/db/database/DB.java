package server.conf.db.database;

import java.util.function.Function;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;

@Component
public class DB {

    private final R2dbcEntityTemplate db;
    private final DatabaseClient dbRaw;
    private final TransactionalOperator trx;

    public DB(ConnectionFactory factory) {
        this.db = new R2dbcEntityTemplate(factory);
        this.dbRaw = db.getDatabaseClient();
        this.trx = TransactionalOperator.create(new R2dbcTransactionManager(factory));
    }

    public <T> Mono<T> trxMono(Function<DatabaseClient, Mono<T>> cb) {
        return cb.apply(dbRaw)
                .as(trx::transactional)
                .onErrorResume(err -> Mono.error(new ErrAPI("trx failed => " + err.getMessage())));
    }

    public <T> Flux<T> trxFlux(Function<DatabaseClient, Flux<T>> cb) {
        return cb.apply(dbRaw)
                .as(trx::transactional)
                .onErrorResume(err -> Flux.error(new ErrAPI("trx failed => " + err.getMessage())));
    }

    public Mono<Void> trx(ConnectionFactory factory,
            Function<Connection, Mono<Void>> cb) {
        return Mono.usingWhen(
                factory.create(),
                cnt -> Mono.from(cnt.beginTransaction())
                        .then(cb.apply(cnt))
                        .then(Mono.from(cnt.commitTransaction())),
                cnt -> Mono.from(cnt.close()),
                (cnt, err) -> Mono.from(cnt.rollbackTransaction()).then(Mono.from(cnt.close())),
                cnt -> Mono.from(cnt.close()));
    }

    public Mono<Integer> truncateAll() {
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                      AND table_type = 'BASE TABLE'
                      AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')
                """;

        return dbRaw.sql(sql)
                .map(row -> row.get("table_name", String.class))
                .all()
                .collectList()
                .flatMap(tables -> {
                    if (tables.isEmpty())
                        return Mono.just(0);

                    String joined = String.join(", ", tables);
                    String truncateSql = "TRUNCATE TABLE " + joined + " RESTART IDENTITY CASCADE";
                    return dbRaw.sql(truncateSql)
                            .fetch()
                            .rowsUpdated()
                            .doOnNext((res) -> {
                                System.out.println(String.format("🪓 truncated %d tables", tables.size()));
                            })
                            .thenReturn(tables.size());
                })
                .onErrorResume(err -> Mono.error(
                        new ErrAPI("failed to truncate tables => " + err.getMessage())));
    }

}
