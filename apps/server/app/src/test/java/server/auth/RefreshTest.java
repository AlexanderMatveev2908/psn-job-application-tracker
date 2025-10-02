package server.auth;

import java.util.Arrays;
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
import server._lib_tests.MyAssrt;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;
import server.lib.data_structure.Prs;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class RefreshTest {
  private final static String URL = "/auth/refresh";

  @Autowired
  private WebTestClient web;
  private ReqT req;

  @BeforeEach
  void setup() {
    req = ReqT.withUrl(web, "/test/user").addQuery("expired[]", "jwt").method(HttpMethod.GET);
  }

  @Test
  void ok() {
    ResT resTk = req.send();

    ResT resErr = ReqT.withUrl(web, "/test/protected").method(HttpMethod.GET).jwt(resTk.getJwt()).send();

    MyAssrt.base(resErr, "jwt_expired", 401);

    ResT resRefresh = ReqT.withUrl(web, URL).jwe(resTk.getJwe()).method(HttpMethod.GET).send();

    MyAssrt.base(resRefresh, 200);
    MyAssrt.hasJwt(resRefresh);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwe_expired", 401), Arguments.of("jwe_not_provided", 401),
        Arguments.of("jwe_invalid", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    if (msg.equals("jwe_expired"))
      req.addQuery("expired[]", "jwe");

    ResT resTk = req.send();

    ReqT reqRefresh = ReqT.withUrl(web, URL).method(HttpMethod.GET);

    if (!msg.equals("jwe_not_provided")) {
      if (!msg.equals("jwe_invalid")) {

        reqRefresh.jwe(resTk.getJwe());
      } else {
        String original = resTk.getJwe();
        String[] parts = original.split("\\.");

        Map<String, Object> header = Prs.base64ToMap(parts[0]);
        header.put("enc", "evil");
        String evilJwe = Prs.mapToBase64(header) + String.join(".", Arrays.copyOfRange(parts, 1, parts.length));

        reqRefresh.jwe(evilJwe);
      }
    }

    ResT resRefresh = reqRefresh.send();

    MyAssrt.base(resRefresh, msg, status);

  }
}