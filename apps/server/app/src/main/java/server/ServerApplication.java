package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import server.decorators.LifeSpawn;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class ServerApplication {

    private final LifeSpawn lifeSpawn;

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

        };
    }

}
