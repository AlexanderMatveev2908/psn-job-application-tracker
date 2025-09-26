package server.conf.cloud.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CloudAssetT {
    IMAGE("image"),
    VIDEO("video");

    private final String val;
}
