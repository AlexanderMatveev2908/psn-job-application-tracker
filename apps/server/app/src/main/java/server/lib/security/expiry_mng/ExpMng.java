package server.lib.security.expiry_mng;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.lib.security.expiry_mng.etc.RecExpJwe;
import server.lib.security.expiry_mng.etc.RecExpJwt;

@SuppressFBWarnings({ "EI" })
@Service
public class ExpMng {

    private final static int EXP_JWT = 15;
    private final static int EXP_JWE = 60;
    private final static int EXP_CBC_HMAC = 15;

    public RecExpJwt jwt() {
        Instant now = Instant.now();
        Date exp = Date.from(now.plus(EXP_JWT, ChronoUnit.MINUTES));

        return new RecExpJwt(Date.from(now), exp);
    }

    public RecExpJwe jwe() {
        Instant now = Instant.now();
        long exp = now.plus(EXP_JWE, ChronoUnit.MINUTES).getEpochSecond();

        return new RecExpJwe(now.getEpochSecond(), exp);
    }

    public long cbcHmac() {
        Instant now = Instant.now();
        long exp = now.plus(EXP_CBC_HMAC, ChronoUnit.MINUTES).getEpochSecond();

        return exp;
    }
}
