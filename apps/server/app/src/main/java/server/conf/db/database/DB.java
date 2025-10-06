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
import server.lib.dev.MyLog;

@Component
public class DB {

    private final R2dbcEntityTemplate dbHigh;
    private final DatabaseClient dbLow;
    private final TransactionalOperator trxMng;
    private final ConnectionFactory factory;

    public DB(ConnectionFactory factory) {
        this.factory = factory;
        this.dbHigh = new R2dbcEntityTemplate(factory);
        this.dbLow = dbHigh.getDatabaseClient();
        this.trxMng = TransactionalOperator.create(new R2dbcTransactionManager(factory));
    }

    public <T> Mono<T> trxMono(Function<DatabaseClient, Mono<T>> cb) {
        return cb.apply(dbLow).as(trxMng::transactional).onErrorResume(err -> Mono.error(new ErrAPI(err.getMessage())));
    }

    public <T> Flux<T> trxFlux(Function<DatabaseClient, Flux<T>> cb) {
        return cb.apply(dbLow).as(trxMng::transactional).onErrorResume(err -> Flux.error(new ErrAPI(err.getMessage())));
    }

    public Mono<Void> trxLow(Function<Connection, Mono<Void>> cb) {
        return Mono.usingWhen(factory.create(),
                cnt -> Mono.from(cnt.beginTransaction()).then(cb.apply(cnt)).then(Mono.from(cnt.commitTransaction())),
                cnt -> Mono.from(cnt.close()),
                (cnt, err) -> Mono.from(cnt.rollbackTransaction()).then(Mono.from(cnt.close())),
                cnt -> Mono.from(cnt.close()));
    }

    public <T> Mono<T> trxHigh(Function<R2dbcEntityTemplate, Mono<T>> cb) {
        return cb.apply(dbHigh).as(trxMng::transactional)
                .onErrorResume(err -> Mono.error(new ErrAPI(err.getMessage())));
    }

    public Mono<Integer> truncateAll() {
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                      AND table_type = 'BASE TABLE'
                      AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')
                """;

        return dbLow.sql(sql).map(row -> row.get("table_name", String.class)).all().collectList().flatMap(tables -> {
            if (tables.isEmpty())
                return Mono.just(0);

            String joined = String.join(", ", tables);
            String truncateSql = "TRUNCATE TABLE " + joined + " RESTART IDENTITY CASCADE";
            return dbLow.sql(truncateSql).fetch().rowsUpdated().doOnNext((res) -> {
                MyLog.log(String.format("🪓 truncated %d tables", tables.size()));
            }).thenReturn(tables.size());
        }).onErrorResume(err -> Mono.error(new ErrAPI(err.getMessage())));
    }

}
