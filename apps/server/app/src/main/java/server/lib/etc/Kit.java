package server.lib.etc;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.env_conf.EnvKeeper;
import server.lib.paths.Hiker;

@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Kit is a bean svc 🫘")
@Service
public class Kit {
    private final ObjectMapper jack;
    private final Hiker hiker;
    private final EnvKeeper envKeeper;

    public Kit(ObjectMapper jack, Hiker hiker, EnvKeeper envKeeper) {
        this.jack = jack;
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
        return this.envKeeper;
    }

}
