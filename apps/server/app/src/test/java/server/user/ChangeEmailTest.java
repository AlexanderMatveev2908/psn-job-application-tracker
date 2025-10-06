package server.user;

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
import server._lib_tests.shapes.ExpArgT;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ChangeEmailTest {
  private final static String URL = "/user/change-email";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private final static Faker faker = new Faker();

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PATCH);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.MANAGE_ACC).send();

    ResT res = mainReq.jwt(resTk.getJwt())
        .body(Map.of("email", faker.internet().emailAddress(), "cbcHmacToken", resTk.getCbcHmac())).send();

    MyAssrt.base(res, 200, "email sent");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("new email must be different from old one", 400),
        Arguments.of("cbc_hmac_expired", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.MANAGE_ACC).expired(ExpArgT.fromSplit(msg)).send();

    var newMail = msg.contains("different from old one") ? resTk.getUser().getEmail() : faker.internet().emailAddress();

    ResT res = mainReq.jwt(resTk.getJwt()).body(Map.of("email", newMail, "cbcHmacToken", resTk.getCbcHmac())).send();

    MyAssrt.base(res, status, msg);

  }
}