package server.decorators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RD;
import server.lib.combo.Kit;
import server.lib.dev.MyLog;

@Service @RequiredArgsConstructor
public final class LifeSpawn {
    private static final String COND = "WHERE table_schema = 'public' "
            + "AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')";
    private final Kit kit;
    private final DB db;
    private final RD rd;

    private Mono<Map<String, Object>> grabTableCount(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT COUNT(*) as count FROM information_schema.tables " + COND).map(row -> {
            Map<String, Object> res = new HashMap<>();
            res.put("count", row.get("count", Integer.class));
            return res;
        }).first();
    }

    private Mono<List<String>> grabTableNames(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT table_name FROM information_schema.tables " + COND)
                .map(row -> row.get("table_name", String.class)).all().collectList();
    }

    @SuppressWarnings({ "unchecked", "UnnecessaryTemporaryOnConversionFromString" })
    public void lifeCheck(WebServerInitializedEvent e) {
        db.trxMono(dbRaw -> grabTableCount(dbRaw).flatMap(res -> grabTableNames(dbRaw).map(tables -> {
            res.put("tables", tables);
            return res;
        }))).flatMap(res -> rd.dbSize().doOnNext(size -> {

            List<String> tables = (List<String>) res.get("tables");
            MyLog.log(String.format("🚀 server running on => %d...", e.getWebServer().getPort()),
                    String.format("⬜ whitelist => %s", kit.getEnvKeeper().getFrontUrl()),
                    String.format("🧮 redis keys => %d", size),
                    String.format("🗃️ db tables => %n%s", MyLog.logCols(tables)));

        }).thenReturn(res)).subscribe(res -> {
        }, err -> MyLog.logErr(err));

    }
}
