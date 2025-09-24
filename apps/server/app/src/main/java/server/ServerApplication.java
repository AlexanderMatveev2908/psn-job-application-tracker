package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.db.DB;
import server.conf.db.RD.RdCmd;
import server.decorators.LifeSpawn;
import server.services.user.UserSvc;

@SpringBootApplication
@ConfigurationPropertiesScan
@SuppressWarnings("unused")
public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    private final RdCmd cmd;
    private final DB db;
    private final UserSvc userSvc;

    @SuppressFBWarnings("EI2")
    public ServerApplication(LifeSpawn lifeSpawn, RdCmd cmd, DB db, UserSvc userSvc) {
        this.lifeSpawn = lifeSpawn;
        this.cmd = cmd;
        this.db = db;
        this.userSvc = userSvc;
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
            // })
            // .subscribe();

            // userSvc.findByEmail("john@gmail.com").subscribe(res -> {
            // System.out.println(res);
            // }, err -> {
            // System.out.println(err.getMessage());
            // });

        };
    }

}
