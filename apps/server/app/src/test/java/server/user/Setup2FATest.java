package server.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class Setup2FATest {
  private final static String URL = "/user/2FA";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PATCH);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.withVerify(web).tokenT(TokenT.MANAGE_ACC).send();

    ResT res = mainReq.jwt(resTk.getJwt()).body(Map.of("cbcHmacToken", resTk.getCbcHmac())).send();

    MyAssrt.base(res, 200);

    var body = res.getBd();

    assertNotNull(body.get("totpSecret"));
    assertNotNull(body.get("backupCodes"));
    assertNotNull(body.get("backupCodes"));
    assertNotNull(body.get("zipFile"));
    assertNotNull(body.get("totpSecretQrCode"));
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("user already has 2FA setup", 409), Arguments.of("jwt_not_provided", 401),
        Arguments.of("cbc_hmac_not_provided", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ResT resTk = GrabTk.withVerify(web).tokenT(TokenT.MANAGE_ACC).send();

    var body = Map.of("cbcHmacToken", resTk.getCbcHmac());

    if (status == 409)
      ReqT.withUrl(web, URL).method(HttpMethod.PATCH).body(body).jwt(resTk.getJwt()).send();

    if (!msg.equals("cbc_hmac_not_provided"))
      mainReq.body(body);

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    ResT res = mainReq.send();

    MyAssrt.base(res, status, msg);

  }
}