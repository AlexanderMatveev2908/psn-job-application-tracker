package server.lib.security.cbc_hmac.etc;

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

    private final AlgT algT;
    private final TokenT tokenT;
    private final byte[] salt;
    private final UUID userId;

    public RecAad(AlgT algT, TokenT tokenT, UUID userId) {
        this.algT = algT;
        this.tokenT = tokenT;
        this.userId = userId;

        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        this.salt = salt;
    }

    public RecAad(String part) {
        try {
            byte[] binary = Frmt.hexToBinary(part);
            String json = new String(binary, StandardCharsets.UTF_8);
            Map<String, Object> map = Frmt.jsonToMap(json);

            this.algT = AlgT.valueOf((String) map.get("algT"));
            this.tokenT = TokenT.valueOf((String) map.get("tokenT"));
            this.salt = Frmt.hexToBinary((String) map.get("salt"));
            this.userId = UUID.fromString((String) map.get("userId"));

        } catch (Exception err) {
            throw new ErrAPI("cbc_hmac_invalid", 401);
        }
    }

    public RecAad(AlgT algT, TokenT tokenT, byte[] salt, UUID userId) {
        this.algT = algT;
        this.tokenT = tokenT;
        this.userId = userId;
        this.salt = salt;
    }

    public byte[] toBinary() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("algT", algT.getValue());
        map.put("tokenT", tokenT.getValue());
        map.put("salt", Frmt.binaryToHex(salt));
        map.put("userId", userId.toString());

        return Frmt.toJson(map).getBytes(StandardCharsets.UTF_8);
    }

}
