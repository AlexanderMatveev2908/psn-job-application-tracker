package server.lib.security.mng_tokens.tokens.jwe.etc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.token.MyToken;

@SuppressFBWarnings({ "EI", "EI2" })
public record RecCreateJweReturnT(MyToken inst, String clientToken) {
}
