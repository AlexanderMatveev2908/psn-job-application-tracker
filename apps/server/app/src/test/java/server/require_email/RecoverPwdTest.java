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
public class RecoverPwdTest {
  private final static String URL = "/require-email/recover-pwd";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).send();
    ResT res = mainReq.body(Map.of("email", resTk.getUser().getEmail())).send();

    MyAssrt.base(res, 200, "email sent");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("user not found", 404));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ResT resTk = GrabTk.with(web).send();

    var email = status == 404 ? new Faker().internet().emailAddress() : resTk.getUser().getEmail();

    ResT res = mainReq.body(Map.of("email", email)).send();

    MyAssrt.base(res, status, msg);
  }
}