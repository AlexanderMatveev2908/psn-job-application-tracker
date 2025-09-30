package server.lib.security.session_tokens;

import server.lib.security.jwe.etc.RecResJwe;

public record RecSessionTokens(RecResJwe recJwe, String jwt) {
}
