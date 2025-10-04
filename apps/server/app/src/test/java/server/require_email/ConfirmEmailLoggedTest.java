package server.require_email;

import java.util.Map;
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

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ConfirmEmailLoggedTest {
  private final static String URL = "/require-email/confirm-email-logged";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private ResT resTk;

  @BeforeEach
  void setup() {
    resTk = GrabTk.with(web).send();
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {
    ResT res = mainReq.jwt(resTk.getJwt()).body(Map.of("email", resTk.getUser().getEmail())).send();

    MyAssrt.base(res, 200, "email sent");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_not_provided", 401), Arguments.of("user already verified", 409),
        Arguments.of("different from account", 409));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    if (msg.contains("already verified"))
      ReqT.verifyMailWithToken(web, resTk.getCbcHmac());

    if (status != 401)
      mainReq.jwt(resTk.getJwt());

    var email = msg.equals("different from account") ? new Faker().internet().emailAddress()
        : resTk.getUser().getEmail();

    ResT res = mainReq.body(Map.of("email", email)).send();

    MyAssrt.base(res, status, msg);
  }
}