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

@SuppressWarnings({ "unchecked", })
@SpringBootTest
@AutoConfigureWebTestClient
@RequiredArgsConstructor
public class DelJobTest {
  private final static String URL = "/job-applications";

  @Autowired
  private WebTestClient web;
  private ReqT mainReq;
  private ResT resTk;
  private UUID jobId;

  @BeforeEach
  void setup() {
    resTk = GrabTk.withVerify(web).send();

    ResT resPost = ReqT.withUrl(web, URL).method(HttpMethod.POST).jwt(resTk.getJwt())
        .multipart(MyPayloads.application()).send();
    var jobAppl = (Map<String, Object>) resPost.getBd().get("jobApplication");
    jobId = UUID.fromString((String) jobAppl.get("id"));

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.DELETE);
  }

  @Test
  void ok() {

    ResT resDel = mainReq.jwt(resTk.getJwt()).pathIdParam(jobId).send();

    MyAssrt.base(resDel, 200);

    ResT resGet = ReqT.withUrl(web, URL).jwt(resTk.getJwt()).pathIdParam(jobId).send();

    MyAssrt.base(resGet, 404, "job application not found");

  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("jwt_not_provided", 401),
        Arguments.of("job application not found", 404), Arguments.of("invalid id", 400));
  }

  @ParameterizedTest
  @MethodSource("badCases")
  void err(String msg, int status) {

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    var id = status == 404 ? UUID.randomUUID() : jobId;

    if (status != 400)
      mainReq.pathIdParam(id);
    else
      mainReq.pathIdInvalid();

    ResT resDel = mainReq.send();

    MyAssrt.base(resDel, status, msg);
  }
}
