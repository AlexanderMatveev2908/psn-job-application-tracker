package server.lib.etc;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import server.conf.env.EnvKeeper;
import server.lib.paths.Hiker;

@Service
public class Kit {
    private final Hiker hiker;
    private static final ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final EnvKeeper envKeeper;

    public Kit(Hiker hiker, EnvKeeper envKeeper) {
        this.hiker = hiker;
        this.envKeeper = envKeeper;
    }

    public Hiker getHiker() {
        return hiker;
    }

    public ObjectMapper getJack() {
        return jack;
    }

    public EnvKeeper getEnvKeeper() {
        return envKeeper;
    }

}
