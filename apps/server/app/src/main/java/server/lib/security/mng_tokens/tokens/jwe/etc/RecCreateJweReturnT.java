package server.lib.security.mng_tokens.tokens.jwe.etc;

import server.models.token.MyToken;

public record RecCreateJweReturnT(MyToken inst, String clientToken) {
}
