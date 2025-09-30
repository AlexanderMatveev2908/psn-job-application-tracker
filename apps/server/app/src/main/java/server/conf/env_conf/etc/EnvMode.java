package server.conf.env_conf.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnvMode {
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