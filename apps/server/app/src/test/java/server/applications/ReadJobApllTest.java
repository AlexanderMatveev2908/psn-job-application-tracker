package server.applications;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
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
import reactor.core.publisher.Mono;
import server._lib_tests.GrabTk;
import server._lib_tests.MyAssrt;
import server._lib_tests.MyPayloads;
import server._lib_tests.ReqT;
import server._lib_tests.ResT;

@SuppressWarnings({ "unused", "unchecked", })
@SpringBootTest
@AutoConfigureWebTestClient
@RequiredArgsConstructor
public class ReadJobApllTest {
  private final static String URL = "/job-applications";

  @Autowired
  private WebTestClient web;
  private ResT resTk;
  private ReqT mainReq;
  private List<Map<String, Object>> jobs = new ArrayList<>();

  @BeforeAll
  static void setupGlobalTimer() {
    System.setProperty("reactor.blocking.timeout", "240s");
  }

  @BeforeEach
  void setup() {
    resTk = GrabTk.withVerify(web).send();

    for (int i = 0; i < 5; i++) {
      ResT resPost = ReqT.withUrl(web, URL).method(HttpMethod.POST).jwt(resTk.getJwt())
          .multipart(MyPayloads.application()).send();
      Map<String, Object> job = (Map<String, Object>) resPost.getBd().get("jobApplication");

      jobs.add(job);
    }

    mainReq = ReqT.withUrl(web, URL).method(HttpMethod.GET);
  }

  @Test
  void ok() {

    String name = (String) jobs.get(0).get("companyName");
    ResT resRead = mainReq.jwt(resTk.getJwt()).addQuery("limit", "1").addQuery("page", "0")
        .addQuery("companyName", name).send();

    MyAssrt.base(resRead, 200);

    var jobsQuery = (List<Map<String, Object>>) resRead.getBd().get("jobApplications");
    var found = false;
    for (var item : jobsQuery) {
      if (((String) item.get("companyName")).equals(name)) {
        found = true;
        break;
      }
    }

    assertTrue(found);
  }

  static Stream<Arguments> badCases() {
    return Stream.of(Arguments.of("page required", 422),
        Arguments.of("limit required", 422),
        Arguments.of("jwt_not_provided", 401));
  }

  @ParameterizedTest
  @MethodSource("badCases")
  void err(String msg, int status) {

    if (!msg.equals("page required"))
      mainReq.addQuery("page", "0");

    if (!msg.equals("limit required"))
      mainReq.addQuery("limit", "1");

    if (!msg.equals("jwt_not_provided"))
      mainReq.jwt(resTk.getJwt());

    ResT resRead = mainReq.send();

    MyAssrt.base(resRead, status, msg);
  }
}