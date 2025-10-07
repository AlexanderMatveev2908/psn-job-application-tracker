package server.verify;

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
import net.datafaker.Faker;
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server._lib_tests.shapes.ExpArgT;
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class VerifyNewEmailTest {
  private final static String URL = "/verify/new-email";
  private final static Faker faker = new Faker();

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private ResT rootResTk;

  @BeforeEach
  void setup() {
    rootResTk = GrabTk.withVerify(web).tokenT(TokenT.MANAGE_ACC).send();

    ReqT.withUrl(web, "/user/change-email").method(HttpMethod.PATCH).jwt(rootResTk.getJwt())
        .addBdCbcHmac(rootResTk.getCbcHmac()).body(Map.of("email", faker.internet().emailAddress())).send();

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.GET);
  }

  @Test
  void ok() {

    ResT resTk = GrabTk.with(web).existingPayload(rootResTk.getUser()).tokenT(TokenT.CHANGE_EMAIL).send();

    ResT resChange = mainReq.addQueryCbcHmac(resTk.getCbcHmac()).send();

    MyAssrt.base(resChange, 200, "email changed");
    MyAssrt.hasTokens(resChange);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("cbc_hmac_expired", 401), Arguments.of("cbc_hmac_wrong_type", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    var tokenT = msg.equals("cbc_hmac_wrong_type") ? TokenT.CHANGE_PWD : TokenT.CHANGE_EMAIL;

    ResT resTk = GrabTk.with(web).existingPayload(rootResTk.getUser()).expired(ExpArgT.fromSplit(msg)).tokenT(tokenT)
        .send();

    ResT resChange = mainReq.addQueryCbcHmac(resTk.getCbcHmac()).send();

    MyAssrt.base(resChange, status, msg);
  }
}