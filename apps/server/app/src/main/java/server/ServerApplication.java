package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import server.decorators.LifeSpawn;
// import server.lib.dev.Dev;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor

public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    // private final Dev dev;

    public static void main(String[] args) {

        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);

            // dev.doStuff();

        };
    }

}
