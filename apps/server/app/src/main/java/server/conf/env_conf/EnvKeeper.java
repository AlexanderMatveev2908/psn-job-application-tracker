package server.conf.env_conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
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

@Data
@Validated
@ConfigurationProperties(prefix = "app")
public class EnvKeeper {

    @NotBlank
    private String appName;

    @NotBlank
    private String nextPblEnv;
    @NotBlank
    private String nextPblBackUrl;
    @NotBlank
    private String nextPblBackUrlDev;
    @NotBlank
    private String nextPblBackUrlTest;
    @NotBlank
    private String nextPblFrontUrl;
    @NotBlank
    private String nextPblFrontUrlDev;
    @NotBlank
    private String nextPblFrontUrlTest;
    @NotBlank
    private String nextPblSmptFrom;

    @NotBlank
    private String redisUrl;

    @NotBlank
    private String smptServer;
    @NotBlank
    private String smptPort;
    @NotBlank
    private String smptUser;
    @NotBlank
    private String smptPwd;

    public EnvMode getEnvMode() {
        return EnvMode.fromValue(this.nextPblEnv);
    }

    public String getFrontUrl() {
        return switch (getEnvMode()) {
            case DEV -> nextPblFrontUrlDev;
            case TEST -> nextPblFrontUrlTest;
            case PROD -> nextPblFrontUrl;
        };
    }

    public String getBackUrl() {
        return switch (getEnvMode()) {
            case DEV -> nextPblBackUrlDev;
            case TEST -> nextPblBackUrlTest;
            case PROD -> nextPblBackUrl;
        };
    }
}