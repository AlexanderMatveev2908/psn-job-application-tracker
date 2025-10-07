package server.verify;

import java.util.HashMap;
import java.util.Map;
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
import net.datafaker.Faker;
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.lib.security.tfa.totp.MyTotp;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ConfirmNewEmailTest {
  private final static String URL = "/verify/new-email-2FA";
  private final static Faker faker = new Faker();

  @Autowired
  private WebTestClient web;
  @Autowired
  private MyTotp totp;
  private ReqT mainReq;
  private ResT resTk;
  private ResT firstCall;

  @BeforeEach
  void setup() {
    resTk = GrabTk.with2FA(web).tokenT(TokenT.MANAGE_ACC).send();

    ResT resRequire = ReqT.withUrl(web, "/user/change-email").method(HttpMethod.PATCH).jwt(resTk.getJwt())
        .body(Map.of("cbcHmacToken", resTk.getCbcHmac(), "email", faker.internet().emailAddress())).send();
    MyAssrt.base(resRequire, 200);

    ResT tokenAsReceivedEmail = GrabTk.with(web).existingPayload(resTk.getUser()).tokenT(TokenT.CHANGE_EMAIL).send();

    firstCall = ReqT.withUrl(web, "/verify/new-email").addQueryCbcHmac(tokenAsReceivedEmail.getCbcHmac()).send();
    MyAssrt.hasCbcHmac(firstCall);

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PATCH);
  }

  static Stream<Arguments> goodCases() {
    return Stream.of(Arguments.of("totp"), Arguments.of("bkpCode"));
  }

  @ParameterizedTest @MethodSource("goodCases")
  void ok(String approach) {
    var body = new HashMap<>();
    body.put("cbcHmacToken", firstCall.getCbcHmac());

    if (approach.equals("totp")) {
      var code = totp.genTestTOTP(resTk.getTotpSecret());
      body.put("totpCode", code);
    } else {
      var code = resTk.getBkpCodes().get(0);
      body.put("backupCode", code);
    }

    ResT resChange = mainReq.body(body).send();

    MyAssrt.hasTokens(resChange);
    MyAssrt.base(resChange, 200);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("totp_code_invalid", 401), Arguments.of("backup_code_invalid", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    var body = new HashMap<>();
    body.put("cbcHmacToken", firstCall.getCbcHmac());

    if (msg.contains("totp")) {
      var code = msg.contains("invalid") ? "123456" : totp.genTestTOTP(resTk.getTotpSecret());
      body.put("totpCode", code);
    } else {
      var code = msg.contains("invalid") ? "AAAA-1111" : resTk.getBkpCodes().get(0);
      body.put("backupCode", code);
    }

    ResT resChange = mainReq.body(body).send();
    MyAssrt.base(resChange, status, msg);
  }
}