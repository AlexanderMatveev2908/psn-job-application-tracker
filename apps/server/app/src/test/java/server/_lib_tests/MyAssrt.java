package server._lib_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import server.conf.Reg;

public class MyAssrt {

    public static String buildStr(Object expected, Object received) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n\n🧪 expected =>\n");
        sb.append(expected);
        sb.append("\n❌ received =>\n");
        sb.append(received + "\n\n");

        return sb.toString();
    }

    public static void assrt(ResT res, String msg, int status) {
        assertEquals(status, res.getStatus(), buildStr(status, res.getStatus()));
        assertTrue((res.getMsg().toLowerCase()).contains(msg.toLowerCase()), buildStr(msg, res.getMsg()));
    }

    public static void assrt(ResT res, int status) {
        assertEquals(status, res.getStatus(), buildStr(status, res.getStatus()));
    }

    public static void assrtSessionTokens(ResT res) {
        assertTrue(Reg.isJWT(res.getJwt()));
        assertTrue(Reg.isJWE(res.getJwe()));
    }
}
