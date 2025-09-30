package server.lib.security.session_tokens;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.lib.security.jwe.MyJwe;
import server.lib.security.jwe.etc.RecResJwe;
import server.lib.security.jwt.MyJwt;

@Service
@RequiredArgsConstructor
public class SessionManager {
    private final MyJwt myJwt;
    private final MyJwe myJwe;

    public String genJwt(UUID userId) {
        return myJwt.create(userId);
    }

    public RecResJwe genJwe(UUID userId) {
        return myJwe.create(userId);
    }

    public RecSessionTokens genSessionTokens(UUID userId) {
        return new RecSessionTokens(genJwe(userId), genJwt(userId));
    }
}
