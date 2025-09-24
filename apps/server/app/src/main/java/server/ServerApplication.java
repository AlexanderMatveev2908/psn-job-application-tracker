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
@SuppressWarnings("unused")
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
    @SuppressWarnings({ "unused", })
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);

            // db.truncateAll()
            // .then(userSvc.createUser(new User("john", "doe", "john@gmail.com", "12345")))
            // .doOnNext(user -> {
            // System.out.println(user);
            // }).flatMap(user -> {
            // return tokenSvc.createToken(user.getId(), TokenT.CHANGE_EMAIL,
            // AlgT.HMAC_SHA256,
            // new byte[0], System.currentTimeMillis() + Duration.ofMinutes(15).toMillis());
            // }).flatMap(newToken -> {
            // System.out.println(newToken);

            // return tokenSvc.createToken(newToken.getUserId(), TokenT.CHANGE_PWD,
            // AlgT.RSA_OAEP256_A256GCM,
            // new byte[0], System.currentTimeMillis() + Duration.ofMinutes(15).toMillis());

            // }).doOnNext(second -> {
            // System.out.println(second);
            // }).flatMap(second -> {

            // return userSvc.getUserPopulated(second.getUserId());
            // }).doOnNext(user -> {
            // System.out.println(user);
            // })
            // .subscribe();

        };
    }

}
