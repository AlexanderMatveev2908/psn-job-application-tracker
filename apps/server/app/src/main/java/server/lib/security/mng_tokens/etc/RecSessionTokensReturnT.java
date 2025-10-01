package server.lib.security.mng_tokens.etc;

import server.lib.security.mng_tokens.tokens.jwe.etc.RecResJwe;

public record RecSessionTokensReturnT(RecResJwe recJwe, String jwt) {
}
