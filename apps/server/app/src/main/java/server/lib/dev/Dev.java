package server.lib.dev;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.lib.security.hkdf.Hkdf;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@SuppressFBWarnings({ "EI2" })
@Service
@RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final RdCmd cmd;
    private final Hkdf hkdf;

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

    public void doStuff() {
        var id = UUID.randomUUID();
        var a = hkdf.derive(AlgT.HMAC_SHA256, TokenT.CHANGE_EMAIL, id, 32);
        var b = hkdf.derive(AlgT.HMAC_SHA256, TokenT.CHANGE_EMAIL, id, 32);

        System.out.println(a.equals(b));
    }

}
