package server.conf.db;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;
import server.decorators.ErrAPI;

@Component
public class DB implements CommandLineRunner {

    private final DatabaseClient db;

    public DB(ConnectionFactory db) {
        this.db = DatabaseClient.create(db);
    }

    public DatabaseClient getDb() {
        return db;
    }

    public void check() {
        db.sql("SELECT NOW() AS ts")
                .map(row -> row.get("ts"))
                .first()
                .doOnNext(val -> System.out.println("🔥 DB connected, time => " + val))
                .doOnError(err -> {
                    throw new ErrAPI("❌ DB connection failed: " + err.getMessage(), 500);
                })
                .block();
    }

    @Override
    public void run(String... args) {
        check();
    }

    public <T> Mono<T> trxRunner(ReactiveTrxCb<T> cb) {
        return db.inConnection(conn -> {
            try {
                return cb.trxCallable(conn);
            } catch (Exception err) {
                return Mono.error(new ErrAPI(err.getMessage(), 500));
            }
        });
    }

    public <T> CompletableFuture<T> trxRunnerAsync(ReactiveTrxCb<T> cb) {
        return trxRunner(cb).toFuture();
    }

    @FunctionalInterface
    public interface ReactiveTrxCb<T> {
        Mono<T> trxCallable(io.r2dbc.spi.Connection conn) throws Exception;
    }
}
