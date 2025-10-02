package server.auth;

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
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server._lib_tests.shapes.ExpArgT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class LogoutTest {
  private final static String URL = "/auth/logout";

  @Autowired
  private WebTestClient web;
  private ReqT req;

  @BeforeEach
  void setup() {
    req = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {
    ResT resTk = ReqT.grabTk(web);

    ResT resLogout = req.jwt(resTk.getJwt()).send();

    MyAssrt.base(resLogout, 200);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_expired", 401), Arguments.of("jwt_not_provided", 401),
        Arguments.of("logged out", 200));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {
    ResT resTk = ReqT.grabTk(web, ExpArgT.JWT);

    if (msg.equals("jwt_expired"))
      req.jwt(resTk.getJwt());
    else if (msg.equals("jwt_not_provided"))
      req.jwe(resTk.getJwe());

    // ? a user with neither jwt or jwe at this point
    // ? first should not be present, but if exists has no sense block
    // ? if he want to go out alone

    ResT resLogout = req.send();

    MyAssrt.base(resLogout, msg, status);

  }
}