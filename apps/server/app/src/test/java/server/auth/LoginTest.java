package server.auth;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor @TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {
  private final static String URL = "/api/v1/auth/login";
  private final static Map<String, Object> registerPayload = MyPayloads.register();

  @Autowired
  private WebTestClient web;
  private ReqT req;

  @BeforeAll
  void register() {
    var resRegister = ReqT.withUrl(web, "/api/v1/auth/register").method(HttpMethod.POST).body(registerPayload).send();
    MyAssrt.assrt(resRegister, "user created", 201);
  }

  @BeforeEach
  void setup() {
    req = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("data not provided", 400, null),
        Arguments.of("wrong data format", 400, "server do not expect a string"),
        Arguments.of("email invalid", 422, MyPayloads.loginPatch(registerPayload, "email", "invalid email @<><><")),
        Arguments.of("invalid password", 401,
            MyPayloads.loginPatch(registerPayload, "password", "wsyX!1}&i5$E&8YyN8K6")),
        Arguments.of("user not found", 404, MyPayloads.login()));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status, Object bd) {

    ResT res = req.body(bd).send();

    MyAssrt.assrt(res, msg, status);
  }
}