package server.conf.cloud.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.decorators.flow.ErrAPI;

@Getter
@RequiredArgsConstructor
public enum CloudResourceT {
    IMAGE("image"),
    VIDEO("video");

    private final String val;

    public static String fromFileField(String field) {
        return switch (field) {
            case "images" -> IMAGE.getVal();
            case "videos" -> VIDEO.getVal();
            default -> throw new ErrAPI("field not supported => " + field);
        };
    }
}
