package server.lib.security.mng_tokens.expiry_mng.etc;

public record RecExpTplSec(long iat, long exp) {

    public RecExpTplDate toDate() {
        return RecExpTplDate.fromSeconds(iat, exp);
    }
}