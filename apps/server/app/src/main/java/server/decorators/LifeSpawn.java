package server.decorators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import server.conf.db.DB;
import server.conf.db.RD.RD;
import server.conf.db.RD.RdCmd;
import server.lib.dev.MyLog;
import server.lib.etc.Kit;

@Service
public class LifeSpawn {
    private static final String COND = "WHERE table_schema = 'public' " +
            "AND table_name NOT IN ('databasechangelog', 'databasechangeloglock')";
    private final Kit kit;
    private final DB db;
    private final RD rd;
    private final RdCmd cmd;

    public LifeSpawn(Kit kit, DB db, RD rd, RdCmd cmd) {
        this.kit = kit;
        this.db = db;
        this.rd = rd;
        this.cmd = cmd;
    }

    private Mono<Map<String, Object>> grabTableCount(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT COUNT(*) as count FROM information_schema.tables " + COND)
                .map(row -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("count", row.get("count", Integer.class));
                    return res;
                })
                .first();
    }

    private Mono<List<String>> grabTableNames(DatabaseClient dbRaw) {
        return dbRaw.sql("SELECT table_name FROM information_schema.tables " + COND)
                .map(row -> row.get("table_name", String.class))
                .all()
                .collectList();
    }

    @SuppressWarnings({ "unchecked", "UnnecessaryTemporaryOnConversionFromString" })
    public void lifeCheck(WebServerInitializedEvent e) {
        db
                .trxRunnerMono(dbRaw -> grabTableCount(dbRaw).flatMap(res -> grabTableNames(dbRaw).map(tables -> {
                    res.put("tables", tables);
                    return res;
                }))).subscribe(res -> {

                    rd.dbSize()
                            .doOnNext(size -> {
                                MyLog.logTtl(String.format("🚀 server running on => %d...", e.getWebServer().getPort()),
                                        String.format("⬜ whitelist => %s", kit.getEnvKeeper().getFrontUrl()),
                                        String.format("🧮 redis keys => %d", size));

                                List<String> tables = (List<String>) res.get("tables");

                                System.out.println("🗃️ db tables => ");
                                MyLog.logCols(tables);
                            })
                            .subscribe();

                    // rd.flushAll().subscribe();

                    // cmd.setStr("some", "abc").subscribe();
                    // cmd.getStr("some").subscribe();
                    // cmd.delK("some").subscribe();

                }, err -> {
                    MyLog.logErr(err);
                });

    }
}
