package server.applications;

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
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SpringBootTest
@AutoConfigureWebTestClient
@RequiredArgsConstructor
@SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
public class PutApplTest {
  private final static String URL = "/job-applications";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private ResT resTk;
  private ResT resPost;
  private UUID pathId;

  @BeforeEach
  void setup() {
    resTk = GrabTk.withVerify(web).send();

    resPost = ReqT.withUrl(web, "/job-applications").jwt(resTk.getJwt()).method(HttpMethod.POST)
        .multipart(MyPayloads.application()).send();

    Map<String, Object> created = (Map<String, Object>) resPost.getBd().get("jobApplication");
    pathId = (UUID.fromString((String) created.get("id")));

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.PUT);
  }

  @Test
  void ok() {

    ResT resPut = mainReq.pathIdParam(pathId).jwt(resTk.getJwt()).multipart(MyPayloads.application()).send();

    MyAssrt.base(resPut, 200, "application updated");
  }

  @Test
  void notMine() {

    ResT otherUser = GrabTk.withVerify(web).send();

    ResT hisPost = ReqT.withUrl(web, "/job-applications").jwt(otherUser.getJwt()).method(HttpMethod.POST)
        .multipart(MyPayloads.application()).send();

    Map<String, Object> hisCreated = (Map<String, Object>) hisPost.getBd().get("jobApplication");
    var hisApplicationId = UUID.fromString((String) hisCreated.get("id"));

    // ? me evil who try t update his application 😈
    ResT resPut = mainReq.pathIdParam(hisApplicationId).jwt(resTk.getJwt()).multipart(MyPayloads.application()).send();

    MyAssrt.base(resPut, 403, "forbidden");
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("invalid job application id", 400), Arguments.of("application not found", 404));
  }

  @ParameterizedTest
  @MethodSource("badCases")
  void err(String msg, int status) {

    if (msg.equals("application not found"))
      mainReq.pathIdParam(UUID.randomUUID());
    else if (!msg.equals("invalid job application id"))
      mainReq.pathIdParam(pathId);

    ResT resPut = mainReq.jwt(resTk.getJwt()).multipart(MyPayloads.application()).send();

    MyAssrt.base(resPut, status, msg);

  }
}
