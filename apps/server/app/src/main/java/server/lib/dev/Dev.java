package server.lib.dev;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.Reg;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.lib.data_structure.Prs;
import server.lib.security.mng_tokens.TkMng;

@SuppressFBWarnings({ "EI2" }) @Service @RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final RdCmd cmd;
    private final TkMng tkMng;

    // @Bean
    // public ApplicationRunner logRoutes(RequestMappingHandlerMapping mapping) {
    // return args -> {
    // mapping.getHandlerMethods().forEach((k, v) -> {
    // System.out.println("📡 " + k + " => " + v);
    // });
    // };
    // }

    public void dropAll() {
        db.truncateAll().flatMap(count -> {
            return cmd.flushAll();
        }).subscribe();
    }

    public void main() {
        String jwt = tkMng.genJwt(UUID.randomUUID());

        String[] parts = jwt.split("\\.");

        for (String p : parts)
            if (Reg.isB64(p))
                System.out.println(Prs.base64ToMap(p));

    }
}
