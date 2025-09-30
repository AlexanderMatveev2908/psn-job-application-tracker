package server.lib.security.hkdf;

import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.springframework.stereotype.Service;

import server.conf.env_conf.EnvKeeper;
import server.lib.security.cbc_hmac.etc.RecAad;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

@Service
public class Hkdf {
    private final Digest digest = new SHA256Digest();
    private final EnvKeeper envKeeper;

    public Hkdf(EnvKeeper envKeeper) {
        this.envKeeper = envKeeper;
    }

    public byte[] derive(RecAad aad, int len) {
        byte[] ikm = envKeeper.getHkdfMaster().getBytes(StandardCharsets.UTF_8);

        byte[] aadBinary = aad.toBinary();

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(digest);
        hkdf.init(new HKDFParameters(ikm, aad.getSalt(), aadBinary));

        byte[] okm = new byte[len];
        hkdf.generateBytes(okm, 0, len);

        return okm;
    }
}
