package server.user;

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

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ManagaAccTest {
  private final static String URL = "/user/manage-account";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {

    ResT resTk = GrabTk.with(web).send();
    ResT resManage = mainReq.jwt(resTk.getJwt()).body(Map.of("password", resTk.getPlainPwd())).send();

    MyAssrt.base(resManage, 200);
    MyAssrt.hasCbcHmac(resManage);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_not_provided", 401), Arguments.of("invalid password", 401),
        Arguments.of("jwt_expired", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ResT resTk = GrabTk.with(web).expired(ExpArgT.fromSplit(msg)).send();

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    var pwd = resTk.getPlainPwd() + (msg.equals("invalid password") ? "invalid.." : "");

    ResT res = mainReq.body(Map.of("password", pwd)).send();

    MyAssrt.base(res, status, msg);
  }
}