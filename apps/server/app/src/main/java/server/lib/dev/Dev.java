package server.lib.dev;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.conf.mail.MailSvc;
import server.decorators.flow.ErrAPI;
import server.lib.security.jwe.MyJwe;
import server.lib.security.jwe.RecJwe;
import server.lib.security.jwt.MyJwt;

@SuppressFBWarnings({ "EI2" })
@Service
@RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final RdCmd cmd;
    private final MailSvc mailSvc;
    private final MyJwt myJwt;
    private final MyJwe myJwe;

    // @Bean
    // public ApplicationRunner logRoutes(RequestMappingHandlerMapping mapping) {
    // return args -> {
    // mapping.getHandlerMethods().forEach((k, v) -> {
    // System.out.println("📡 " + k + " => " + v);
    // });
    // };
    // }

    public void doMailTxtStuff() {
        mailSvc.sendRctTxtMail("matveevalexander470@gmail.com", "🧪 TEST EMAIL", "testing java mail service")
                .subscribe();
    }

    public void doMailHtmlStuff() {
        mailSvc.sendRctHtmlMail("matveevalexander470@gmail.com", "🧪 TEST EMAIL", "john")
                .subscribe();
    }

    public void dropAll() {
        db.truncateAll().flatMap(count -> {
            return cmd.flushAll();
        }).subscribe();
    }

    public void doJwtStuff() {
        var token = myJwt.create(UUID.randomUUID());

        System.out.println(token);

        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3)
                throw new ErrAPI("invalid jwt");

            Base64.Decoder urlDecoder = Base64.getUrlDecoder();
            ObjectMapper om = new ObjectMapper();

            String headerJson = new String(urlDecoder.decode(parts[0]), StandardCharsets.UTF_8);
            JsonNode header = om.readTree(headerJson);
            System.out.println("hdr => " + om.writerWithDefaultPrettyPrinter().writeValueAsString(header));

            String payloadJson = new String(urlDecoder.decode(parts[1]), StandardCharsets.UTF_8);
            JsonNode payload = om.readTree(payloadJson);
            System.out.println("payload => " + om.writerWithDefaultPrettyPrinter().writeValueAsString(payload));

            byte[] signature = urlDecoder.decode(parts[2]);
            System.out.println("bytes => " + signature.length);

            var res = myJwt.check(token);
            System.out.println(res.getSubject());
        } catch (Exception err) {
            MyLog.logErr(err);
        }
    }

    public void doJweStuff() {
        try {
            RecJwe rec = myJwe.create(UUID.randomUUID());
            myJwe.check(rec.token());
        } catch (Exception err) {
            MyLog.logErr(err);
        }
    }
}
