package server.auth;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import server._lib_tests.MyAssrt;
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest @AutoConfigureWebTestClient()
public class RegisterTest {

  private final static String URL = "/auth/register";

  @Autowired
  private WebTestClient web;

  private ReqT req;

  @BeforeEach
  void setup() {
    req = ReqT.withUrl(web, URL);
  }

  @Test
  void ok() {
    ResT res = req.method(HttpMethod.POST).body(MyPayloads.register()).send();

    MyAssrt.assrt(res, "user created", 201);

    MyAssrt.assrtSessionTokens(res);

  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("data not provided", 400, null),
        Arguments.of("wrong data format", 400, "server do not expect a string as body"),
        Arguments.of("first name invalid", 422,
            MyPayloads.registerPatch("firstName", "<script>alert(\"hacked😈\")</script>")),
        Arguments.of("last name required", 422, MyPayloads.registerPatch("lastName", "")),
        Arguments.of("email invalid", 422, MyPayloads.registerPatch("email", "@@@invalid....email?")),
        Arguments.of("password invalid", 422,
            MyPayloads.changeValByKey(MyPayloads.registerPatch("password", "123"), "confirmPassword", "123")),
        Arguments.of("passwords do not match", 422,
            MyPayloads.registerPatch("confirmPassword", "different from password")),
        Arguments.of("an account with this email already exists", 409, MyPayloads.register()));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status, Object bd) {
    ResT res = req.method(HttpMethod.POST).body(bd).send();

    if (!msg.contains("already exists")) {
      MyAssrt.assrt(res, msg, status);
      return;
    }

    ResT secondCall = req.method(HttpMethod.POST).body(bd).send();
    MyAssrt.assrt(secondCall, msg, status);

  }
}
