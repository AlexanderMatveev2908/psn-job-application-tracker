package server.lib.security.mng_tokens.etc;

import server.lib.security.mng_tokens.tokens.jwe.etc.RecCreateJweReturnT;

public record RecSessionTokensReturnT(RecCreateJweReturnT jwe, String jwt) {
}
