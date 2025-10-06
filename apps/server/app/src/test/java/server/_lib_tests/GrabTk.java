package server._lib_tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server._lib_tests.shapes.ExpArgT;
import server.models.token.etc.TokenT;
import server.models.user.User;

@SuppressFBWarnings({ "EI2", })
public class GrabTk {
  private final WebTestClient web;
  private final List<ExpArgT> expired = new ArrayList<>();
  private User existingPayload;
  private TokenT tokenT;
  private boolean verifyUser;

  private GrabTk(WebTestClient web) {
    this.web = web;
  }

  public static GrabTk with(WebTestClient web) {
    return new GrabTk(web);
  }

  public static GrabTk withVerify(WebTestClient web) {
    return new GrabTk(web).verifyUser();
  }

  public GrabTk tokenT(TokenT tokenT) {
    this.tokenT = tokenT;
    return this;
  }

  public GrabTk verifyUser() {
    this.verifyUser = true;
    return this;
  }

  public GrabTk expired(ExpArgT arg) {
    this.expired.add(arg);
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

    if (verifyUser)
      req.addQuery("verifyUser", "true");

    for (ExpArgT exp : expired)
      if (exp != null)
        req.addQuery("expired[]", exp.getValue());

    if (existingPayload != null)
      req.body(Map.of("existingPayload", existingPayload));

    return req.send();
  }
}
