package server.lib.security.mng_tokens.expiry_mng.etc;

import java.time.Instant;
import java.util.Date;

public record RecExpTplDate(Date iat, Date exp) {

    public static RecExpTplDate fromSeconds(long iatSec, long expSec) {
        return new RecExpTplDate(Date.from(Instant.ofEpochSecond(iatSec)), Date.from(Instant.ofEpochSecond(expSec)));
    }
}
