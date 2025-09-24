package server.lib.etc;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.env_conf.EnvKeeper;

@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Kit is a bean svc 🫘")
@Service
public class Kit {
    private final ObjectMapper jack;
    private final EnvKeeper envKeeper;

    public Kit(ObjectMapper jack, EnvKeeper envKeeper) {
        this.jack = jack;
        this.envKeeper = envKeeper;
    }

    public ObjectMapper getJack() {
        return jack;
    }

    public EnvKeeper getEnvKeeper() {
        return this.envKeeper;
    }

}
