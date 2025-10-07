package server.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
public class ManageAcc2FATest {
  private final static String URL = "/user/manage-account-2FA";

  @Autowired
  private WebTestClient web;
  @Autowired
  MyTotp totp;
  private ResT resTk;
  private ResT firstCall;
  private ReqT mainReq;

  @BeforeEach
  void setup() {

    resTk = GrabTk.with2FA(web).send();

    firstCall = ReqT.withUrl(web, "/user/manage-account").method(HttpMethod.POST).jwt(resTk.getJwt())
        .body(Map.of("password", resTk.getPlainPwd())).send();

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
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

    ResT resManage = mainReq.jwt(resTk.getJwt()).body(body).send();

    MyAssrt.hasCbcHmac(resManage);
    MyAssrt.base(resManage, 200);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_not_provided", 401, (Function<String, String>) (val -> val)),
        Arguments.of("jwt_not_provided", 401, (Function<String, String>) (val -> val)),
        Arguments.of("cbc_hmac_not_provided", 401, (Function<String, String>) (val -> val)),
        Arguments.of("totp_code_invalid", 401, (Function<String, String>) (val -> "123456")),
        Arguments.of("backup_code_invalid", 401, (Function<String, String>) (val -> "AAAA-1111")),
        Arguments.of("backup_code_invalid", 422, (Function<String, String>) (val -> "AAAA-")));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status, Function<String, String> modifyCode) {

    var body = new HashMap<>();

    if (!msg.equals("cbc_hmac_not_provided"))
      body.put("cbcHmacToken", firstCall.getCbcHmac());

    if (msg.contains("totp")) {
      var code = totp.genTestTOTP(resTk.getTotpSecret());
      body.put("totpCode", modifyCode.apply(String.valueOf(code)));
    } else {
      var code = resTk.getBkpCodes().get(0);
      body.put("backupCode", modifyCode.apply(code));
    }

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    ResT resManage = mainReq.body(body).send();

    MyAssrt.base(resManage, status, msg);

  }
}