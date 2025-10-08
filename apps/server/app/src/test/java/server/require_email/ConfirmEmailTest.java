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
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ConfirmEmailTest {
  private final static String URL = "/require-email/confirm-email";

  @Autowired
  private WebTestClient web;
  private final Faker faker = new Faker();
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.CONF_EMAIL).send();

    ResT resRequire = mainReq.body(Map.of("email", resTk.getUser().getEmail())).send();

    MyAssrt.base(resRequire, 200, "email sent");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("user not found", 404), Arguments.of("user already verified", 409),
        Arguments.of("email required", 422));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ResT resTk = GrabTk.with(web).send();

    if (msg.contains("already verified"))
      ReqT.withUrl(web, "/verify/confirm-email").method(HttpMethod.GET).addQueryCbcHmac(resTk.getCbcHmac()).send();

    var email = status == 404 ? faker.internet().emailAddress() : resTk.getUser().getEmail();
    mainReq.body(Map.of(status == 422 ? "mail" : "email", email));

    ResT resRequire = mainReq.send();

    MyAssrt.base(resRequire, status, msg);

  }
}