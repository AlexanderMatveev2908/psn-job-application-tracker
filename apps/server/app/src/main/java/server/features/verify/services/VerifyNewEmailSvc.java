package server.features.verify.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.api.Api;
import server.lib.dev.MyLog;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyNewEmailSvc {

  private final UserRepo userRepo;
  private final TokenRepo tokenRepo;
  private final TokenCombo tokenCombo;

  public Mono<Tuple2<ResponseCookie, String>> mgn(Api api) {

    var user = api.getUser();

    return userRepo.toggleEmails(user.getId()).then(tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CHANGE_EMAIL)
        .collectList().doOnNext(ids -> MyLog.log("🧹 tokens deleted => " + ids.size())))
        .then(tokenCombo.genSessionTokens(user));
  }
}
