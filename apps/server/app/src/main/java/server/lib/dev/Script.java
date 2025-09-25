package server.lib.dev;

import java.time.Duration;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
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
public class Script {
    // private final RdCmd cmd;
    private final DB db;
    private final UserSvc userSvc;
    private final TokenSvc tokenSvc;
    private final BkpCodesSvc backupCodeSvc;

    public void doStuff() {
        db.truncateAll()
                .flatMap(res -> {

                    return userSvc.createUser(
                            new User("john", "doe", "john@gmail.com", "12345"));
                })
                .flatMap(user -> {

                    return tokenSvc.createToken(
                            user.getId(),
                            TokenT.CHANGE_EMAIL,
                            AlgT.HMAC_SHA256,
                            "12345",
                            System.currentTimeMillis() + Duration.ofMinutes(15).toMillis());
                }).flatMap(token -> backupCodeSvc.insert(token.getUserId(), "12345"))
                .flatMap(code -> {
                    return userSvc.getUserPop(code.getUserId());
                })
                .doOnNext(user -> {

                    MyLog.wLogOk(user);

                })
                .subscribe(res -> {
                }, err -> {

                    MyLog.logErr(err);

                });
    }
}
