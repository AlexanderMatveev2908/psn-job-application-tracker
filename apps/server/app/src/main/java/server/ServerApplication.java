package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import server.conf.env.EnvKeeper;

@SpringBootApplication
public class ServerApplication {

    private final EnvKeeper env;

    public ServerApplication(EnvKeeper env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    @SuppressWarnings("unused")
    ApplicationListener<WebServerInitializedEvent> lifeSpawn() {
        return e -> {

            System.out.println(String.format("🚀 server running on %d...", e.getWebServer().getPort()));
            System.out.println();
            System.out.println("⬜ whitelist => " + env.getFrontUrl());
        };
    }
}
