package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.decorators.LifeSpawn;
import server.models.token.svc.TokenSvc;
import server.models.user.svc.UserSvc;

@SpringBootApplication
@ConfigurationPropertiesScan
@SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    private final RdCmd cmd;
    private final DB db;
    private final UserSvc userSvc;
    private final TokenSvc tokenSvc;

    @SuppressFBWarnings("EI2")
    public ServerApplication(LifeSpawn lifeSpawn, RdCmd cmd, DB db, UserSvc userSvc, TokenSvc tokenSvc) {
        this.lifeSpawn = lifeSpawn;
        this.cmd = cmd;
        this.db = db;
        this.userSvc = userSvc;
        this.tokenSvc = tokenSvc;
    }

    public static void main(String[] args) {

        SpringApplication.run(ServerApplication.class, args);
    }

    // @Bean
    // public ApplicationRunner logRoutes(RequestMappingHandlerMapping mapping) {
    // return args -> {
    // mapping.getHandlerMethods().forEach((key, value) -> {
    // System.out.println("📡 Route =>" + key + " → " + value);
    // });
    // };
    // }

    @Bean
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);

            // db.truncateAll()
            // .flatMap(res -> {

            // return userSvc.createUser(

            // new User("john", "doe", "john@gmail.com", "12345"));
            // })
            // .flatMap(user -> {

            // return tokenSvc.createToken(
            // user.getId(),
            // TokenT.CHANGE_EMAIL,
            // AlgT.HMAC_SHA256,
            // "12345",
            // System.currentTimeMillis() + Duration.ofMinutes(15).toMillis());
            // })
            // .flatMap(token -> {

            // return userSvc.getUserPop(token.getUserId());
            // })
            // .doOnNext(user -> {
            // System.out.println(user);
            // MyLog.asyncLog(user);

            // })
            // .subscribe(res -> {
            // }, err -> {
            // System.out.println(err.getMessage());
            // });

        };
    }

}
