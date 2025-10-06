package server.lib.security.tfa.totp.etc;

public record RecTotpSecret(String clientSecret, String uri, String encrypted) {
}