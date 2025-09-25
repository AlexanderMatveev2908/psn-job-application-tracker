package server.conf.cloud.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CloudAssetT {
    IMAGE("images"),
    VIDEO("vidoes");

    private final String val;
}
