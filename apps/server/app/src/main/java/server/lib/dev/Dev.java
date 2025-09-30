package server.lib.dev;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.lib.security.cbc_hmac.CbcHmac;
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
    private final CbcHmac cbcHmac;

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

    public void doAesHmacStuff() {

        var usId = UUID.randomUUID();
        var token = cbcHmac.create(AlgT.AES_CBC_HMAC_SHA256, TokenT.CHANGE_EMAIL, usId);

        System.out.println(token);

        var res = cbcHmac.check(token.getHashed(), AlgT.AES_CBC_HMAC_SHA256, TokenT.CHANGE_EMAIL, usId);

        System.out.println(res);
    }

}
