package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import server.decorators.LifeSpawn;

@SpringBootApplication
public class ServerApplication {

    private final LifeSpawn lifeSpawn;

    public ServerApplication(LifeSpawn lifeSpawn) {
        this.lifeSpawn = lifeSpawn;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    @SuppressWarnings({ "unused", })
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);
        };
    }

}
