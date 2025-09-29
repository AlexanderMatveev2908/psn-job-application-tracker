package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.LifeSpawn;
import server.lib.dev.MyLog;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor

public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    // private final Dev dev;
    private final EnvKeeper envKeeper;

    public static void main(String[] args) {

        try {

            SpringApplication.run(ServerApplication.class, args);

        } catch (Exception err) {
            MyLog.logErr(err);
        }
    }

    @Bean
    ApplicationListener<WebServerInitializedEvent> startCheck() {
        return e -> {

            lifeSpawn.lifeCheck(e);

            System.out.println(envKeeper.getJwtSecret());

            // dev.dropAll();

        };
    }

}
