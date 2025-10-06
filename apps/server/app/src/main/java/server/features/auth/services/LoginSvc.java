package server.features.auth.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.user.User;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class LoginSvc {

  private final TokenCombo tokenCombo;

  public Mono<Tuple2<ResponseCookie, String>> simpleLogin(User user) {
    return tokenCombo.genSessionTokens(user);
  }

  public Mono<String> firstStepLogin2FA(User user) {
    return tokenCombo.insertCbcHmac(user.getId(), TokenT.LOGIN_2FA);
  }

}
