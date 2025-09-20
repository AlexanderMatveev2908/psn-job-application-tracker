package server.conf.db;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import server.decorators.ErrAPI;

@Component
public class DB implements CommandLineRunner {
    private final DataSource db;
    private final PlatformTransactionManager trx;

    public DB(DataSource db, PlatformTransactionManager trx) {
        this.db = db;
        this.trx = trx;
    }

    public DataSource getDataSource() {
        return db;
    }

    public void check() throws Exception {
        // try (Connection cnt = db.getConnection()) {
        // System.out.println("🔥 db => " + cnt.getMetaData().getDatabaseProductName());
        // }
    }

    @Override
    public void run(String... args) throws Exception {
        check();
    }

    @FunctionalInterface
    public interface TrxCb<T> {
        T trxCallable(Connection conn) throws Exception;
    }

    public <T> T trxRunner(TrxCb<T> cb) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = trx.getTransaction(def);

        try (Connection cnt = db.getConnection()) {

            T result = cb.trxCallable(cnt);
            trx.commit(status);
            return result;
        } catch (Exception err) {
            trx.rollback(status);

            throw new ErrAPI(err.getMessage(), err instanceof ErrAPI ? ((ErrAPI) err).getStatus() : 500);
        }
    }

    public <T> CompletableFuture<T> trxRunnerAsync(TrxCb<T> cb) {
        return CompletableFuture.supplyAsync(() -> trxRunner(cb));
    }
}
