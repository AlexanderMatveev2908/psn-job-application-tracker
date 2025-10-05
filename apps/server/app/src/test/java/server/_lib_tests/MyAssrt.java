package server._lib_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import server.conf.Reg;
import server.lib.data_structure.ShapeCheck;

public class MyAssrt {

    public static void base(ResT res, int status, String msg) {
        assertEquals(status, res.getStatus());

        if (Stream.of(msg, res.getMsg()).allMatch(ShapeCheck::isStr))
            assertTrue(res.getMsg().toLowerCase().contains(msg.toLowerCase()));

    }

    public static void base(ResT res, int status) {
        base(res, status, null);
    }

    public static void hasJwt(ResT res) {
        assertTrue(Reg.isJWT(res.getJwt()));
    }

    public static void hasTokens(ResT res) {
        hasJwt(res);
        assertTrue(Reg.isJWE(res.getJwe()));
    }

    public static void hasCbcHmac(ResT res) {
        assertTrue(Reg.isCbcHmac(res.getCbcHmac()));
    }
}
