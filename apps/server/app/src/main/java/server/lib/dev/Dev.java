package server.lib.dev;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.conf.mail.MailSvc;

@SuppressFBWarnings({ "EI2" })
@Service
@RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final RdCmd cmd;
    private final MailSvc mailSvc;

    // @Bean
    // public ApplicationRunner logRoutes(RequestMappingHandlerMapping mapping) {
    // return args -> {
    // mapping.getHandlerMethods().forEach((k, v) -> {
    // System.out.println("📡 " + k + " => " + v);
    // });
    // };
    // }

    public void doMailHtmlStuff() {
        mailSvc.sendRctHtmlMail("matveevalexander470@gmail.com", "🧪 TEST EMAIL", "john")
                .subscribe();
    }

    public void dropAll() {
        db.truncateAll().flatMap(count -> {
            return cmd.flushAll();
        }).subscribe();
    }

}
