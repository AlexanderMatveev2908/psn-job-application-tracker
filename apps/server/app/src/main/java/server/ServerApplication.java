package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.db.RD.RdCmd;
import server.decorators.LifeSpawn;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ServerApplication {

    private final LifeSpawn lifeSpawn;
    @SuppressWarnings("unused")
    private final RdCmd cmd;

    @SuppressFBWarnings("EI2")
    public ServerApplication(LifeSpawn lifeSpawn, RdCmd cmd) {
        this.lifeSpawn = lifeSpawn;
        this.cmd = cmd;
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

        };
    }

}
