package server.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
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

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor @Timeout(value = 60, unit = TimeUnit.SECONDS)
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

  static Stream<Arguments> goodCases() {
    return Stream.of(Arguments.of("totp"), Arguments.of("bkp_code"));
  }

  @ParameterizedTest @MethodSource("goodCases")
  void ok(String approach) {

    var body = new HashMap<>();
    body.put("cbcHmacToken", resFirstStep.getCbcHmac());

    if (approach.equals("totp")) {
      var code = totp.genTestTOTP(resTk.getTotpSecret());
      body.put("totpCode", code);
    } else {
      body.put("backupCode", resTk.getBkpCodes().get(0));
    }

    ResT res = mainReq.body(body).send();

    MyAssrt.hasTokens(res);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("totp_code_invalid", 401), Arguments.of("backup_code_invalid", 401),
        Arguments.of("cbc_hmac_not_provided", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    var body = new HashMap<>();

    if (!msg.equals("cbc_hmac_not_provided"))
      body.put("cbcHmacToken", resFirstStep.getCbcHmac());

    if (msg.contains("totp")) {
      var code = msg.equals("totp_code_invalid") ? "123456" : totp.genTestTOTP(resTk.getTotpSecret());

      body.put("totpCode", code);
    } else {
      var code = msg.equals("backup_code_invalid") ? "AAAA-1111" : resTk.getBkpCodes().get(0);

      body.put("backupCode", code);
    }

    ResT res = mainReq.body(body).send();

    MyAssrt.base(res, status, msg);
  }
}