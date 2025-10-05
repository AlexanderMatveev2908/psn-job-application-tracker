package server.verify;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
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
import server.models.token.etc.TokenT;

@SpringBootTest @AutoConfigureWebTestClient @RequiredArgsConstructor
public class ConfirmEmailTest {
  private final static String URL = "/verify/confirm-email";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;

  @BeforeEach
  void setup() {
    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.GET);
  }

  @Test
  void ok() {
    ResT resTk = GrabTk.with(web).tokenT(TokenT.CONF_EMAIL).send();

    ResT resVerify = mainReq.addCbcHmac(resTk.getCbcHmac()).send();

    MyAssrt.base(resVerify, 200, "user verified");
    MyAssrt.hasTokens(resVerify);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("cbc_hmac_not_provided", 401), Arguments.of("cbc_hmac_expired", 401),
        Arguments.of("cbc_hmac_invalid", 401), Arguments.of("user already verified", 409),
        Arguments.of("cbc_hmac_wrong_type", 401));
  }

  @ParameterizedTest @MethodSource("badCases")
  void err(String msg, int status) {

    GrabTk reqTk = GrabTk.with(web).tokenT(msg.equals("cbc_hmac_wrong_type") ? TokenT.RECOVER_PWD : TokenT.CONF_EMAIL)
        .expired(ExpArgT.fromSplit(msg));

    if (status == 409) {
      ResT firstCall = GrabTk.with(web).tokenT(TokenT.CONF_EMAIL).send();
      ResT resVerifyFirstCall = ReqT.withUrl(web, URL).addQuery("cbcHmacToken", firstCall.getCbcHmac()).send();
      MyAssrt.hasTokens(resVerifyFirstCall);

      reqTk.existingPayload(firstCall.getUser());
    }

    ResT resTk = reqTk.send();

    if (!msg.equals("cbc_hmac_not_provided"))
      if (!msg.equals("cbc_hmac_invalid")) {
        mainReq.addCbcHmac(resTk.getCbcHmac());
      } else {
        String parts[] = resTk.getCbcHmac().split("\\.");
        Map<String, Object> map = Prs.hexToMap(parts[0]);
        map.put("tokenT", TokenT.CHANGE_EMAIL.getValue());
        String evilPart = Prs.mapToHex(map);

        String evilCbcHmac = Stream
            .concat(Stream.of(evilPart), Arrays.stream(Arrays.copyOfRange(parts, 1, parts.length)))
            .collect(Collectors.joining("."));

        mainReq.addCbcHmac(evilCbcHmac);

      }

    ResT resVerify = mainReq.send();

    MyAssrt.base(resVerify, status, msg);

  }
}