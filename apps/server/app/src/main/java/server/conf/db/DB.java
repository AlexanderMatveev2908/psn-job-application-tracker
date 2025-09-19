package server.conf.db;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DB implements CommandLineRunner {
    private final DataSource db;

    public DB(DataSource db) {
        this.db = db;
    }

    public void check() throws Exception {
        try (Connection cnt = db.getConnection()) {
            System.out.println("🔥 db => " + cnt.getMetaData().getDatabaseProductName());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        check();
    }
}
