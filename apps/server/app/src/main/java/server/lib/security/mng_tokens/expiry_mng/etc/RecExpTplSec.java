package server.lib.security.mng_tokens.expiry_mng.etc;

import java.time.Instant;
import java.util.Date;

public record RecExpTplSec(long iat, long exp) {

    public Date toDate(long arg) {
        return Date.from(Instant.ofEpochSecond(arg));
    }
}