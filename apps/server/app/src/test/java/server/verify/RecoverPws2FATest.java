package server.verify;

import java.util.HashMap;
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
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.lib.security.tfa.totp.MyTotp;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class RecoverPws2FATest {
  private final static String URL = "/verify/recover-pwd-2FA";

  @Autowired
  private WebTestClient web;
  @Autowired
  private MyTotp totp;
  private ReqT mainReq;
  private ResT resTk;

  @BeforeEach
  void setup() {
    resTk = GrabTk.with2FA(web).tokenT(TokenT.RECOVER_PWD).send();

    ReqT.withUrl(web, "/verify/recover-pwd").addQueryCbcHmac(resTk.getCbcHmac()).send();

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  static Stream<Arguments> goodCases() {
    return Stream.of(Arguments.of("totp"), Arguments.of("bkp"));
  }

  @ParameterizedTest @MethodSource("goodCases")
  void ok(String approach) {
    var body = new HashMap<>();

    body.put("cbcHmacToken", resTk.getCbcHmac());

    if (approach.equals("totp"))
      body.put("totpCode", totp.genTestTOTP(resTk.getTotpSecret()));
    else
      body.put("backupCode", resTk.getBkpCodes().get(0));

    ResT res = mainReq.body(body).send();

    MyAssrt.base(res, 200);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("totp_code_invalid", 401), Arguments.of("backup_code_invalid", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {
    var body = new HashMap<>();

    body.put("cbcHmacToken", resTk.getCbcHmac());

    if (msg.contains("totp"))
      body.put("totpCode", msg.contains("invalid") ? "123456" : totp.genTestTOTP(resTk.getTotpSecret()));
    else
      body.put("backupCode", msg.contains("invalid") ? "AAAA-FFFF" : resTk.getBkpCodes().get(0));

    ResT res = mainReq.body(body).send();

    MyAssrt.base(res, status, msg);
  }
}