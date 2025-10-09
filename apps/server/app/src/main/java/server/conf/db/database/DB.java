package server.conf.db.database;

import java.util.function.Function;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;

@Component
public class DB {

    private final R2dbcEntityTemplate orm;
    private final DatabaseClient sqlClient;
    private final TransactionalOperator trxMng;
    private final ConnectionFactory factory;

    public DB(ConnectionFactory factory) {
        this.factory = factory;
        this.orm = new R2dbcEntityTemplate(factory);
        this.sqlClient = orm.getDatabaseClient();
        this.trxMng = TransactionalOperator.create(new R2dbcTransactionManager(factory));
    }

    public <T> Mono<T> trxMono(Function<DatabaseClient, Mono<T>> cb) {
        return cb.apply(sqlClient).as(trxMng::transactional)
                .onErrorResume(err -> Mono.error(new ErrAPI(err.getMessage())));
    }

    public <T> Mono<T> trxLow(Function<Connection, Mono<T>> cb) {
        return Mono.usingWhen(
                factory.create(),
                cnt -> Mono.from(cnt.beginTransaction())
                        .then(cb.apply(cnt))
                        .flatMap(result -> Mono.from(cnt.commitTransaction()).thenReturn(result)),
                cnt -> Mono.from(cnt.close()),
                (cnt, err) -> Mono.from(cnt.rollbackTransaction()).then(Mono.from(cnt.close())),
                cnt -> Mono.from(cnt.close()));
    }

    public <T> Mono<T> trxHigh(Function<R2dbcEntityTemplate, Mono<T>> cb) {
        return cb.apply(orm).as(trxMng::transactional)
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

        return sqlClient.sql(sql).map(row -> row.get("table_name", String.class)).all().collectList()
                .flatMap(tables -> {
                    if (tables.isEmpty())
                        return Mono.just(0);

                    String joined = String.join(", ", tables);
                    String truncateSql = "TRUNCATE TABLE " + joined + " RESTART IDENTITY CASCADE";
                    return sqlClient.sql(truncateSql).fetch().rowsUpdated().thenReturn(tables.size());
                }).onErrorResume(err -> Mono.error(new ErrAPI(err.getMessage())));
    }

}
