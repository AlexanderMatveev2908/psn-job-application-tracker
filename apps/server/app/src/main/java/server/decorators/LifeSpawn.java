package server.decorators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import server.conf.db.DB;
import server.lib.dev.MyLog;
import server.lib.etc.Kit;

@Service
public class LifeSpawn {
    private final Kit kit;
    private final DB db;
    private final String cond = "WHERE table_schema = 'public' " +
            "AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')";

    public LifeSpawn(Kit kit, DB db) {
        this.kit = kit;
        this.db = db;
    }

    private Mono<Map<String, Object>> grabTableCount(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT COUNT(*) as count FROM information_schema.tables " + cond)
                .map(row -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("count", row.get("count", Integer.class));
                    return res;
                })
                .first();
    }

    private Mono<List<String>> grabTableNames(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT table_name FROM information_schema.tables " + cond)
                .map(row -> row.get("table_name", String.class))
                .all()
                .collectList();
    }

    @SuppressWarnings("unchecked")
    public void lifeCheck(WebServerInitializedEvent e) {
        Mono<Map<String, Object>> result = db
                .trxRunnerMono(dbRaw -> grabTableCount(dbRaw).flatMap(res -> grabTableNames(dbRaw).map(tables -> {
                    res.put("tables", tables);
                    return res;
                })));

        result.subscribe(res -> {
            MyLog.logTtl(String.format("🚀 server running on => %d...", e.getWebServer().getPort()),
                    String.format("⬜ whitelist => %s", kit.getEnvKeeper().getFrontUrl()));

            List<String> tables = (List<String>) res.get("tables");
            StringBuilder sb = new StringBuilder(
                    String.format("🔥 db tables count => %d%n", res.get("count")));

            for (int i = 0; i < tables.size(); i++) {
                sb.append(String.format("%-20s|", tables.get(i)));
                if ((i + 1) % 3 == 0)
                    sb.append("\n");
            }
            if (tables.size() % 3 != 0)
                sb.append("\n");

            System.out.println(sb.toString());
        }, err -> {
            throw new ErrAPI(err.getMessage(),
                    err instanceof ErrAPI ? ((ErrAPI) err).getStatus() : 500);
        });
    }
}
