package server.verify;

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
  private final static String URL = "/verify/recover-pwd";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.GET);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.RECOVER_PWD).send();

    ResT mainRes = mainReq.addCbcHmac(resTk.getCbcHmac()).send();

    MyAssrt.base(mainRes, 200);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("cbc_hmac_not_provided", 401), Arguments.of("cbc_hmac_expired", 401),
        Arguments.of("cbc_hmac_wrong_type", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {
    ResT resTk = GrabTk.with(web).tokenT(msg.contains("wrong_type") ? TokenT.CHANGE_EMAIL : TokenT.RECOVER_PWD)
        .expired(ExpArgT.fromSplit(msg)).send();

    if (!msg.equals("cbc_hmac_not_provided"))
      mainReq.addCbcHmac(resTk.getCbcHmac());

    ResT res = mainReq.send();

    MyAssrt.base(res, status, msg);
  }
}