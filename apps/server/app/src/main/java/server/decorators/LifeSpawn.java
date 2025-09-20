package server.decorators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.stereotype.Service;

import server.conf.db.DB;
import server.lib.dev.MyLog;
import server.lib.etc.Kit;

@Service
public class LifeSpawn {
    private final Kit kit;
    private final DB db;

    public LifeSpawn(Kit kit, DB db) {
        this.kit = kit;
        this.db = db;
    }

    @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch" })
    public void lifeCheck(WebServerInitializedEvent e) {

        db.trxRunnerAsync(cnt -> {
            int count = 0;
            try (PreparedStatement stmt = cnt.prepareStatement(
                    "SELECT COUNT(*) FROM information_schema.tables " +
                            "WHERE table_schema = 'public' " +
                            "AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')");
                    ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    count = rs.getInt(1);

            }

            List<String> tableNames = new ArrayList<>();
            try (PreparedStatement stmt = cnt.prepareStatement(
                    "SELECT table_name FROM information_schema.tables " +
                            "WHERE table_schema = 'public' " +
                            "AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')");

                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next())
                    tableNames.add(rs.getString("table_name"));

            }

            Map<String, Object> data = new HashMap<>();
            data.put("count", count);
            data.put("tables", tableNames);

            return data;
        }).thenAccept(res -> {
            MyLog.logTtl(String.format("🚀 server running on => %d...", e.getWebServer().getPort()),
                    String.format("⬜ whitelist => %s", kit.getEnvKeeper().getFrontUrl()));

            List<String> tables = (List<String>) res.get("tables");
            StringBuilder sb = new StringBuilder(
                    String.format("🔥 db tables count => %d%n", (Integer) res.get("count")));
            for (int i = 0; i < tables.size(); i++) {
                sb.append(String.format("%-20s|", tables.get(i)));
                if ((i + 1) % 3 == 0)
                    sb.append("\n");
            }

            if (tables.size() % 3 != 0)
                sb.append("\n");

            System.out.println(sb.toString());
        }).exceptionally(err -> {
            throw new ErrAPI(err.getMessage(), err instanceof ErrAPI ? ((ErrAPI) err).getStatus() : 500);
        });

    }
}
