package server.conf.env_conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.etc.Resolved;

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
public final class EnvKeeper {

    @NotBlank
    @Resolved
    private String appName;

    @NotBlank
    @Resolved
    private String nextPblEnv;
    @NotBlank
    @Resolved
    private String nextPblBackUrl;
    @NotBlank
    @Resolved
    private String nextPblBackUrlDev;
    @NotBlank
    @Resolved
    private String nextPblBackUrlTest;
    @NotBlank
    @Resolved
    private String nextPblFrontUrl;
    @NotBlank
    @Resolved
    private String nextPblFrontUrlDev;
    @NotBlank
    @Resolved
    private String nextPblFrontUrlTest;
    @NotBlank
    @Resolved
    private String nextPblSmptFrom;

    @NotBlank
    @Resolved
    private String cloudName;
    @NotBlank
    @Resolved
    private String cloudKey;
    @NotBlank
    @Resolved
    private String cloudSecret;

    @NotBlank
    @Resolved
    private String redisUrl;

    @NotBlank
    @Resolved
    private String jwtSecret;
    @NotBlank
    @Resolved
    private String jwePrivate;
    @NotBlank
    @Resolved
    private String jwePublic;

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