package server.lib.security.hkdf;

import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.springframework.stereotype.Service;

import server.conf.env_conf.EnvKeeper;
import server.lib.data_structure.Frmt;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

@Service
public class Hkdf {
    private final Digest digest = new SHA256Digest();
    private final EnvKeeper envKeeper;

    public Hkdf(EnvKeeper envKeeper) {
        this.envKeeper = envKeeper;
    }

    public String derive(AlgT algT, TokenT tokenT, UUID userId, int len) {
        byte[] ikm = envKeeper.getHkdfMaster().getBytes(StandardCharsets.UTF_8);

        byte[] salt = envKeeper.getHkdfSalt().getBytes(StandardCharsets.UTF_8);

        byte[] info = Frmt.toJson(Map.of(
                "algT", algT,
                "tokenT", tokenT,
                "userId", userId)).getBytes(StandardCharsets.UTF_8);

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(digest);

        hkdf.init(new HKDFParameters(ikm, salt, info));

        byte[] derived = new byte[len];
        hkdf.generateBytes(derived, 0, len);

        return HexFormat.of().formatHex(derived);
    }
}
