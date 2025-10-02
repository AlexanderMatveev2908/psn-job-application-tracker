package server.lib.security.hash.hash_methods;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Prs;

@SuppressFBWarnings({ "EI2", "REC" }) @Service @RequiredArgsConstructor
public class DbHash {
    private final EnvKeeper envKeeper;

    public String hash(String arg) {
        try {

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(Prs.utf8ToBinary(envKeeper.getDbHashKey()), "HmacSHA256"));
            return Prs.binaryToHex(mac.doFinal(Prs.utf8ToBinary(arg)));
        } catch (Exception err) {
            throw new ErrAPI("err hashing db token");
        }
    }

    public boolean check(String hashed, String clientToken) {
        try {
            String recomputed = hash(clientToken);

            return MessageDigest.isEqual(Prs.utf8ToBinary(hashed), Prs.utf8ToBinary(recomputed));

        } catch (Exception err) {
            throw new ErrAPI("err checking db token");
        }
    }

}
