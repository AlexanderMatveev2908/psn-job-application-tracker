package server.lib.security.cbc_hmac.etc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.token.MyToken;

@SuppressFBWarnings({ "EI2", "EI" })
public record RecCreateCbcHmac(MyToken token, String clientToken) {
}
