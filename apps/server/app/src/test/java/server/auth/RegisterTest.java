package server.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.RequiredArgsConstructor;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest
@AutoConfigureWebTestClient
@RequiredArgsConstructor
public class RegisterTest {

    private final static String URL = "/api/v1/auth/register";

    @Autowired
    private WebTestClient web;
    private ReqT req;

    @BeforeEach
    void setup() {
        req = new ReqT(web);
    }

    @Test
    void err400() {
        ResT res = req.reqBd(URL, HttpMethod.POST, null);

        assertEquals(res.getStatus(), 400);
    }

}
