package server.auth;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.RequiredArgsConstructor;
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.lib.security.tfa.totp.MyTotp;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class Login2FATest {
  private final static String URL = "/auth/login-2FA";

  @Autowired
  private WebTestClient web;
  @Autowired
  private MyTotp totp;
  private ReqT mainReq;
  private ResT resTk;
  private ResT resFirstStep;

  @BeforeEach
  void setup() {
    resTk = GrabTk.with2FA(web).send();

    var body = Map.of("email", resTk.getUser().getEmail(), "password", resTk.getPlainPwd());

    resFirstStep = ReqT.withUrl(web, "/auth/login").method(HttpMethod.POST).body(body).send();

    MyAssrt.hasCbcHmac(resFirstStep);

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {

    var code = totp.genTestTOTP(resTk.getTotpSecret());

    var body = Map.of("cbcHmacToken", resFirstStep.getCbcHmac(), "totpCode", code);

    ResT res = mainReq.body(body).send();

    MyAssrt.hasTokens(res);
  }

  // static Stream<Arguments> badCases() {
  // return Stream.of(Arguments.of());
  // }
  //
  // @ParameterizedTest @MethodSource("badCases")
  // void err(String msg, int status) {
  //
  // }
}