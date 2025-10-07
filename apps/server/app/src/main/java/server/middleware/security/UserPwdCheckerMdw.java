package server.middleware.security;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.lib.security.hash.MyHashMng;

@Service @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class UserPwdCheckerMdw {
  private final MyHashMng hashMng;

  public Mono<Void> checkUserPwd(Api api, String plainText, boolean mustMatch) {
    var user = api.getUser();

    if (user == null)
      throw new ErrAPI("passed null in mdw which expected user instance");

    return hashMng.argonCheck(user.getPassword(), plainText).flatMap(resCheck -> {
      if (!resCheck && mustMatch)
        return Mono.error(new ErrAPI("invalid password", 401));
      else if (resCheck && !mustMatch)
        return Mono.error(new ErrAPI("new password must be different from old one", 400));

      return Mono.empty();
    });
  }
}
