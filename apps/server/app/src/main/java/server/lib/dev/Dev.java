package server.lib.dev;

import java.time.Duration;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.mail.MailSvc;
import server.models.applications.etc.JobApplStatusT;
import server.models.applications.svc.JobApplRepo;
// import server.conf.db.remote_dictionary.RdCmd;
import server.models.backup_code.svc.BkpCodesSvc;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenSvc;
import server.models.user.User;
import server.models.user.svc.UserSvc;

@SuppressFBWarnings({ "EI2" })
@Service
@RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final UserSvc userSvc;
    private final TokenSvc tokenSvc;
    private final BkpCodesSvc backupCodeSvc;
    private final JobApplRepo jobApplRepo;
    private final MailSvc mailSvc;

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

    public void doStuff() {
        db.truncateAll()
                .flatMap(res -> userSvc.insert(
                        new User("john", "doe", "john@gmail.com", "12345")))
                .flatMap(user -> tokenSvc.createToken(
                        user.getId(),
                        TokenT.CHANGE_EMAIL,
                        AlgT.HMAC_SHA256,
                        "12345",
                        System.currentTimeMillis() + Duration.ofMinutes(15).toMillis()))
                .flatMap(token -> backupCodeSvc.insert(token.getUserId(), "12345"))
                .flatMap(code -> jobApplRepo.insert(code.getUserId(), "cool company name", "highly paid position",
                        JobApplStatusT.OFFER, null))
                .flatMap(appl -> userSvc.getUserPop(appl.getUserId()))
                .doOnNext(user -> {

                    MyLog.wOk(user);

                })
                .subscribe(res -> {
                }, err -> {

                    MyLog.logErr(err);

                });
    }
}
