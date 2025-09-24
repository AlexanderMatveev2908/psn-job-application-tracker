package server.models.token.side;

public enum AlgT {
    AES_CBC_HMAC_SHA256("AES_CBC_HMAC_SHA256"),
    RSA_OAEP256_A256GCM("RSA_OAEP256_A256GCM"),
    HMAC_SHA256("HMAC_SHA256");

    private final String value;

    AlgT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
