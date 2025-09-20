package server.lib.etc;

import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import server.conf.env.EnvKeeper;

@Service
public class Kit {
    private final Path serverDir = Hiker.grabDir();
    private final ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final EnvKeeper envKeeper;

    public Kit(EnvKeeper envKeeper) {
        this.envKeeper = envKeeper;
    }

    public Path getServerDir() {
        return serverDir;
    }

    public ObjectMapper getJack() {
        return jack;
    }

    public EnvKeeper getEnvKeeper() {
        return envKeeper;
    }

}
