package server.features.verify.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.Api;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenComboSvc;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyMailSvc {

  private final UserRepo userRepo;
  private final TokenRepo tokenRepo;

  private final TokenComboSvc tokenComboSvc;

  public Mono<Tuple2<ResponseCookie, String>> mng(Api api) {
    var user = api.getUser();

    return userRepo.verifyUser(user.getId())
        .then(tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CONF_EMAIL).then())
        .then(tokenComboSvc.genSessionTokens(user));
  }
}
