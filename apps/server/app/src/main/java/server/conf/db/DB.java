package server.conf.db;

import java.util.function.Function;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.decorators.ErrAPI;

@Component
public class DB {

    private final R2dbcEntityTemplate db;
    private final TransactionalOperator trx;
    private final DatabaseClient dbRaw;

    public DB(ConnectionFactory cnt) {
        this.db = new R2dbcEntityTemplate(cnt);
        ReactiveTransactionManager trxMng = new R2dbcTransactionManager(cnt);
        this.trx = TransactionalOperator.create(trxMng);
        this.dbRaw = db.getDatabaseClient();
    }

    public R2dbcEntityTemplate getDb() {
        return db;
    }

    public <T> Mono<T> trxRunnerMono(Function<DatabaseClient, Mono<T>> cb) {
        return cb.apply(dbRaw)
                .as(trx::transactional)
                .onErrorResume(err -> Mono.error(new ErrAPI("❌ trx failed => " + err.getMessage(), 500)));
    }

    public <T> Flux<T> trxRunnerFlux(Function<DatabaseClient, Flux<T>> cb) {
        return cb.apply(dbRaw)
                .as(trx::transactional)
                .onErrorResume(err -> Flux.error(new ErrAPI("❌ trx failed => " + err.getMessage(), 500)));
    }

}
