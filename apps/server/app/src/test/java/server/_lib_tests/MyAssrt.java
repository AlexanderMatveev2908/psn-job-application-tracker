package server._lib_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyAssrt {
    public static void assrt(ResT res, String msg, int status) {
        assertEquals(status, res.getStatus(),
                String.format("❌ expected status %d • 🚦 received %d", status, res.getStatus()));
        assertTrue((res.getMsg()).contains(msg),
                String.format("❌ expected msg %s • 📝 received %s", msg, res.getMsg()));
    }
}
