package server.lib.security.mng_tokens.tokens.cbc_hmac.etc;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@Getter
public class RecAad {
    private static final SecureRandom secureRandom = new SecureRandom();

    private final AlgT algT;
    private final TokenT tokenT;
    private final byte[] salt;
    private final UUID userId;
    private final UUID tokenId;

    public RecAad(TokenT tokenT, UUID userId) {
        this.algT = AlgT.AES_CBC_HMAC_SHA256;
        this.tokenT = tokenT;
        this.userId = userId;

        byte[] salt = new byte[32];
        secureRandom.nextBytes(salt);
        this.salt = salt;
        this.tokenId = UUID.randomUUID();
    }

    private RecAad(AlgT algT, TokenT tokenT, byte[] salt, UUID userId, UUID tokenId) {
        this.algT = algT;
        this.tokenT = tokenT;
        this.salt = salt;
        this.userId = userId;
        this.tokenId = tokenId;
    }

    public byte[] getSalt() {
        return salt.clone();
    }

    public static RecAad fromPart(String part) {
        try {
            byte[] binary = Frmt.hexToBinary(part);
            String json = new String(binary, StandardCharsets.UTF_8);
            Map<String, Object> map = Frmt.jsonToMap(json);

            AlgT algT = AlgT.valueOf((String) map.get("algT"));
            TokenT tokenT = TokenT.valueOf((String) map.get("tokenT"));
            byte[] salt = Frmt.hexToBinary((String) map.get("salt"));
            UUID userId = UUID.fromString((String) map.get("userId"));
            UUID tokenId = UUID.fromString((String) map.get("tokenId"));

            return new RecAad(algT, tokenT, salt, userId, tokenId);
        } catch (Exception err) {
            throw new ErrAPI("cbc_hmac_invalid", 401);
        }
    }

    public byte[] toBinary() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("algT", algT.getValue());
        map.put("tokenT", tokenT.getValue());
        map.put("salt", Frmt.binaryToHex(salt));
        map.put("userId", userId.toString());
        map.put("tokenId", tokenId.toString());

        return Frmt.mapToBinary(map);
    }

}
