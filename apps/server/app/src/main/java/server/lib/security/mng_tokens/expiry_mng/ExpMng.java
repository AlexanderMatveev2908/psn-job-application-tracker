package server.lib.security.mng_tokens.expiry_mng;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplDate;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;

@SuppressFBWarnings({ "EI" })
@Service
public class ExpMng {

    private final static int EXP_JWT = 15;
    private final static int EXP_JWE = 60;
    private final static int EXP_CBC_HMAC = 15;

    private RecExpTplSec genTpl(int arg) {
        Instant now = Instant.now();
        long exp = now.plus(arg, ChronoUnit.MINUTES).getEpochSecond();

        return new RecExpTplSec(now.getEpochSecond(), exp);

    }

    public RecExpTplDate jwt() {
        return genTpl(EXP_JWT).toDate();
    }

    public RecExpTplSec jwe() {

        return genTpl(EXP_JWE);
    }

    public RecExpTplSec cbcHmac() {
        return genTpl(EXP_CBC_HMAC);
    }
}
