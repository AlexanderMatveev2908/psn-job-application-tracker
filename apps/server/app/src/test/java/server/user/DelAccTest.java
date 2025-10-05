package server.user;

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
public class DelAccTest {
  private final static String URL = "/user/delete-account";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.DELETE);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.MANAGE_ACC).send();

    ResT res = mainReq.jwt(resTk.getJwt()).addCbcHmac(resTk.getCbcHmac()).send();

    MyAssrt.base(res, 200, "account deleted");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("cbc_hmac_not_provided", 401), Arguments.of("cbc_hmac_expired", 401),
        Arguments.of("jwt_expired", 401), Arguments.of("jwt_not_provided", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.MANAGE_ACC).expired(ExpArgT.fromSplit(msg)).send();

    if (!msg.equals("cbc_hmac_not_provided"))
      mainReq.addCbcHmac(resTk.getCbcHmac());

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    ResT res = mainReq.send();

    MyAssrt.base(res, status, msg);
  }
}