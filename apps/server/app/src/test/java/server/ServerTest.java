package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerTest {

    @Value("${server.port}")
    private int port;

    @Test
    void contextLoads() {
    }

    @Test
    void serverLogOnConsole() {
        assertEquals(3000, port, "server should run on 3000");
    }
}
