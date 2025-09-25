package server.models.token.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlgT {
    AES_CBC_HMAC_SHA256("AES_CBC_HMAC_SHA256"),
    RSA_OAEP256_A256GCM("RSA_OAEP256_A256GCM"),
    HMAC_SHA256("HMAC_SHA256");

    private final String value;

}
