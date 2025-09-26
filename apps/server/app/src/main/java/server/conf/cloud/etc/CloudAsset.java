package server.conf.cloud.etc;

import lombok.Data;

@Data
public class CloudAsset {
    private final String publicId;
    private final String url;
    private final String resourceType;

}
