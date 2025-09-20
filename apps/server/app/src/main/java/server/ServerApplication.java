package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import server.decorators.LifeSpawn;
import server.services.UserSvc;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    private final UserSvc userSvc;

    public ServerApplication(LifeSpawn lifeSpawn, UserSvc userSvc) {
        this.lifeSpawn = lifeSpawn;
        this.userSvc = userSvc;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    @SuppressWarnings({ "unused", })
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);

            var users = this.userSvc.findAll();
            System.out.println(users);
        };
    }

}
