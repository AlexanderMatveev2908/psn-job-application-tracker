package server.lib.security.cbc_hmac.etc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import server.lib.data_structure.Frmt;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

public record RecAad(AlgT algT, TokenT tokenT, UUID userId) {

    public byte[] toBinary() {
        return Frmt.toJson(this).getBytes(StandardCharsets.UTF_8);
    }

}
