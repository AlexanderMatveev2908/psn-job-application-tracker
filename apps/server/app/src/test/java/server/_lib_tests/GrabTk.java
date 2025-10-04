package server._lib_tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import server._lib_tests.shapes.ExpArgT;
import server.models.token.etc.TokenT;
import server.models.user.User;

public class GrabTk {
  private final WebTestClient web;
  private final List<ExpArgT> expired = new ArrayList<>();
  private User existingPayload;
  private TokenT tokenT;

  private GrabTk(WebTestClient web) {
    this.web = web;
  }

  public static GrabTk with(WebTestClient web) {
    return new GrabTk(web);
  }

  public GrabTk token(TokenT tokenT) {
    this.tokenT = tokenT;
    return this;
  }

  public GrabTk expired(ExpArgT... args) {
    this.expired.addAll(Arrays.asList(args));
    return this;
  }

  public GrabTk existingPayload(User user) {
    this.existingPayload = user;
    return this;
  }

  public ResT send() {
    ReqT req = ReqT.withUrl(web, "/test/user").method(HttpMethod.POST);

    if (tokenT != null)
      req.addQuery("tokenT", tokenT.getValue());

    for (ExpArgT exp : expired)
      if (exp != null)
        req.addQuery("expired[]", exp.getValue());

    if (existingPayload instanceof User)
      req.body(Map.of("existingPayload", existingPayload));

    return req.send();
  }
}
