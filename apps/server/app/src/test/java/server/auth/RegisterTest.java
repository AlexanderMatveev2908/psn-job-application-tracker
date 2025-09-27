package server.auth;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.RequiredArgsConstructor;
import server._lib_tests.MyAssrt;
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

    static Stream<Arguments> errCases() {
        return Stream.of(
                Arguments.of("data not provided", 400, null),
                Arguments.of("wrong data format", 400, "server do not expect a string as body"));
    }

    @ParameterizedTest
    @MethodSource("errCases")
    void err(String msg, int status, Object bd) {
        ResT res = req.reqBd(URL, HttpMethod.POST, bd);

        MyAssrt.assrt(res, msg, status);
    }
}
