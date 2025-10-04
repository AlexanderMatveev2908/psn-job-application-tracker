package server.auth;

import java.util.Map;
import java.util.UUID;
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
import server.lib.data_structure.Prs;

@SuppressWarnings({ "unused", "UseSpecificCatch",
    "CallToPrintStackTrace" }) @SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ProtectedTest {

  private ResT resTok;

  @Autowired
  private WebTestClient web;

  @BeforeEach
  void setup() {
    resTok = GrabTk.with(web).expired(ExpArgT.JWT).send();
  }

  @Test
  void ok() {

    ResT resTokens = GrabTk.with(web).send();
    ResT resProtected = ReqT.withUrl(web, "/test/protected").method(HttpMethod.GET).jwt(resTokens.getJwt()).send();

    MyAssrt.base(resProtected, 200, "here you are protected data");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_expired", 401), Arguments.of("jwt_not_provided", 401),
        Arguments.of("jwt_invalid", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    ReqT reqProtected = ReqT.withUrl(web, "/test/protected").method(HttpMethod.GET);

    if (msg.equals("jwt_expired")) {
      reqProtected.jwt(resTok.getJwt());
    } else if (msg.equals("jwt_invalid")) {
      String[] parts = resTok.getJwt().split("\\.");
      Map<String, Object> payload = Prs.base64ToMap(parts[1]);

      Map<String, Object> evilSub = Prs.jsonToMap((String) payload.get("sub"));
      String evilId = UUID.randomUUID().toString();
      String evilJwt = parts[0] + Prs.mapToBase64(payload) + parts[2];

      reqProtected.jwt(evilJwt);
    }

    ResT resProtected = reqProtected.send();

    MyAssrt.base(resProtected, status, msg);
  }
}