package server.lib.security.hash;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;

@SuppressFBWarnings({ "EI2", "REC" })
@Service
@RequiredArgsConstructor
public class DbHash {
    private final EnvKeeper envKeeper;

    public String hash(String plain) {
        try {

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(Frmt.utf8ToBinary(envKeeper.getDbHashKey()), "HmacSHA256"));
            return Frmt.binaryToHex(mac.doFinal(Frmt.utf8ToBinary(plain)));
        } catch (Exception err) {
            throw new ErrAPI("err hashing db token");
        }
    }
}
