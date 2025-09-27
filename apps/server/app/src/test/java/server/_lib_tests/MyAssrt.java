package server._lib_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyAssrt {

    public static String buildStr(Object expected, Object received) {
        StringBuilder sb = new StringBuilder();

        sb.append("🧪 expected =>%n");
        sb.append(expected);
        sb.append("❌ received =>%n");
        sb.append(received);

        return sb.toString();
    }

    public static void assrt(ResT res, String msg, int status) {
        assertEquals(status, res.getStatus(),
                buildStr(status, res.getStatus()));
        assertTrue((res.getMsg()).contains(msg),
                buildStr(msg, res.getMsg()));
    }
}
