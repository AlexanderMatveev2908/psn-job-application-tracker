package server.lib.combo;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;

@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Kit is a bean svc 🫘")
@Service
@Getter
@RequiredArgsConstructor
public class Kit {
    private final ObjectMapper jack;
    private final EnvKeeper envKeeper;

}
