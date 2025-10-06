package server.auth;

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
import server._lib_tests.shapes.ExpArgT;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class RecoverPwdTest {
  private final static String URL = "/auth/recover-pwd";
  private final static String newPwd = "wsyX!1}&i5$E&8YyN8K6";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PATCH);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.RECOVER_PWD).send();

    ResT resChange = mainReq.body(Map.of("cbcHmacToken", resTk.getCbcHmac(), "password", newPwd)).send();

    MyAssrt.base(resChange, 200, "password changed");
    MyAssrt.hasTokens(resChange);

  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("new password must be different from old one", 400),
        Arguments.of("cbc_hmac_expired", 401), Arguments.of("cbc_hmac_not_provided", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ResT resTk = GrabTk.with(web).tokenT(TokenT.RECOVER_PWD).expired(ExpArgT.fromSplit(msg)).send();

    var pwd = status == 400 ? resTk.getPlainPwd() : newPwd;
    var token = msg.equals("cbc_hmac_not_provided") ? "" : resTk.getCbcHmac();

    ResT resChange = mainReq.body(Map.of("cbcHmacToken", token, "password", pwd)).send();

    MyAssrt.base(resChange, status, msg);

  }
}