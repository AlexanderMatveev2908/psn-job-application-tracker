package server.lib.security.mng_tokens.tokens.cbc_hmac.etc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.token.MyToken;

@SuppressFBWarnings({ "EI2", "EI" })
public record RecCreateCbcHmacReturnT(MyToken token, String clientToken) {
}
