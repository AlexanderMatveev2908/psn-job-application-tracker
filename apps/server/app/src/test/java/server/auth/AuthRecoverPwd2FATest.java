package server.auth;

import java.util.HashMap;
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
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.lib.security.tfa.totp.MyTotp;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class AuthRecoverPwd2FATest {
  private final static String URL = "/auth/recover-pwd-2FA";

  @Autowired
  private WebTestClient web;
  @Autowired
  private MyTotp totp;
  private ReqT mainReq;
  private ResT resTk;
  private ResT firstCall;

  @BeforeEach
  void setup() {
    resTk = GrabTk.with2FA(web).tokenT(TokenT.RECOVER_PWD).send();

    var body = Map.of("totpCode", totp.genTestTOTP(resTk.getTotpSecret()), "cbcHmacToken", resTk.getCbcHmac());
    firstCall = ReqT.withUrl(web, "/verify/recover-pwd-2FA").method(HttpMethod.POST).body(body).send();

    MyAssrt.hasCbcHmac(firstCall);

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PATCH);
  }

  @Test
  void ok() {
    var body = new HashMap<>();
    body.put("cbcHmacToken", firstCall.getCbcHmac());
    body.put("password", "wsyX!1}&i5$E&8YyN8K6");

    ResT res = mainReq.body(body).send();

    MyAssrt.hasTokens(res);
    MyAssrt.base(res, 200);
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