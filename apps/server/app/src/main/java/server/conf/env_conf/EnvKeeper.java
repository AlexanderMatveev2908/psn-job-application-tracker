package server.conf.env_conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum EnvMode {
    DEV("development"),
    TEST("test"),
    PROD("production");

    private final String val;

    public static EnvMode fromValue(String val) {
        for (EnvMode mode : values())
            if (mode.val.equalsIgnoreCase(val))
                return mode;

        throw new IllegalArgumentException("❌ unknown env mode => " + val);
    }
}

@ConfigurationProperties(prefix = "app")
public class EnvKeeper {

    private static final List<String> REQUIRED_KEYS = List.of(
            "appName",
            "nextPblEnv",
            "nextPblBackUrl",
            "nextPblBackUrlDev",
            "nextPblBackUrlTest",
            "nextPblFrontUrl",
            "nextPblFrontUrlDev",
            "nextPblFrontUrlTest",
            "nextPblSmptFrom",
            "redisUrl",
            "smptServer",
            "smptPort",
            "smptUser",
            "smptPwd");

    private Map<String, String> envVars = new HashMap<>();

    public Map<String, String> getEnvVars() {
        return Collections.unmodifiableMap(this.envVars);
    }

    public void setEnvVars(Map<String, String> props) {

        for (String k : REQUIRED_KEYS) {
            String v = props.get(k);

            if (!props.containsKey(k))
                throw new IllegalArgumentException("❌ missing env key => " + k);
            else if (v == null || v.isBlank())
                throw new IllegalArgumentException("❌ empty key => " + k);
            else if (v.startsWith(" ") || v.endsWith(" "))
                throw new IllegalArgumentException(String.format("❌ Key %s %s with a space", k,
                        v.startsWith(" ") ? "starts" : "ends"));
        }

        this.envVars = Map.copyOf(props);
    }

    public String get(String key) {
        return this.envVars.get(key);
    }

    public String getMode() {
        return this.envVars.get("nextPblEnv");
    }

    public String getFrontUrl() {
        EnvMode envMode = EnvMode.fromValue(this.envVars.get("nextPblEnv"));

        return switch (envMode) {
            case DEV -> this.envVars.get("nextPblFrontUrlDev");
            case TEST -> this.envVars.get("nextPblFrontUrlTest");
            case PROD -> this.envVars.get("nextPblFrontUrl");
        };
    }

    public String getBackUrl() {
        EnvMode envMode = EnvMode.fromValue(this.envVars.get("nextPblEnv"));

        return switch (envMode) {
            case DEV -> this.envVars.get("nextPblBackUrlDev");
            case TEST -> this.envVars.get("nextPblBackUrlTest");
            case PROD -> this.envVars.get("nextPblBackUrl");
        };
    }
}