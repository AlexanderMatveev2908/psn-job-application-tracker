package server.applications;

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
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest
@AutoConfigureWebTestClient
@RequiredArgsConstructor
public class CreateApplTest {
  private final static String URL = "/job-applications";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private ResT resTk;

  @BeforeEach
  void setup() {
    resTk = GrabTk.withVerify(web).send();

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.POST);
  }

  @Test
  void ok() {
    ResT res = mainReq.multipart(MyPayloads.application()).jwt(resTk.getJwt()).send();

    MyAssrt.base(res, 201, "application created");
  }

  @Test
  void notVerifiedUser() {
    ResT resNotVerified = GrabTk.with(web).send();

    ResT resPost = ReqT.withUrl(web, "/job-applications").jwt(resNotVerified.getJwt()).method(HttpMethod.POST)
        .multipart(MyPayloads.application()).send();

    MyAssrt.base(resPost, 403, "user not verified");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("company name required", 422, MyPayloads.applicationPatch("companyName", "")),
        Arguments.of("date invalid", 422, MyPayloads.applicationPatch("appliedAt", "2025-16-35")),
        Arguments.of("invalid enum", 422, MyPayloads.applicationPatch("status", "not as real enum")),
        Arguments.of("jwt_not_provided", 401, MyPayloads.application()));
  }

  @ParameterizedTest
  @MethodSource("badCases")
  void err(String msg, int status, Map<String, Object> payload) {

    if (status != 401)
      mainReq.jwt(resTk.getJwt());

    ResT res = mainReq.multipart(payload).send();

    MyAssrt.base(res, status, msg);
  }
}
