package server.lib.security.mng_tokens;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.lib.security.mng_tokens.tokens.cbc_hmac.MyCbcHmac;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.lib.security.mng_tokens.tokens.jwe.MyJwe;
import server.lib.security.mng_tokens.tokens.jwe.etc.RecCreateJweReturnT;
import server.lib.security.mng_tokens.tokens.jwt.MyJwt;
import server.models.token.etc.TokenT;

@Service
@RequiredArgsConstructor
public class MyTkMng {
    private final MyJwt myJwt;
    private final MyJwe myJwe;
    private final MyCbcHmac myCbcHmac;

    public String genJwt(UUID userId) {
        return myJwt.create(userId);

    }

    public RecCreateJweReturnT genJwe(UUID userId) {
        return myJwe.create(userId);
    }

    public RecSessionTokensReturnT genSessionTokens(UUID userId) {
        return new RecSessionTokensReturnT(genJwe(userId), genJwt(userId));
    }

    public RecCreateCbcHmacReturnT genCbcHmac(TokenT tokenT, UUID userId) {
        return myCbcHmac.create(tokenT, userId);
    }
}
