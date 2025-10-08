package server.features.verify.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.api.Api;
import server.models.backup_code.svc.BkpCombo;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyNewEmailSvc {

  private final UserRepo userRepo;
  private final TokenRepo tokenRepo;
  private final TokenCombo tokenCombo;
  private final BkpCombo bkpCombo;

  public Mono<Tuple2<ResponseCookie, String>> simpleFlow(Api api) {
    var user = api.getUser();
    return userRepo.toggleEmails(user.getId())
        .then(tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CHANGE_EMAIL).collectList())
        .then(tokenCombo.genSessionTokens(user.getId()));
  }

  public Mono<String> firstSetp2FA(Api api) {
    var user = api.getUser();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CHANGE_EMAIL).collectList()
        .then(tokenCombo.insertCbcHmac(user.getId(), TokenT.CHANGE_EMAIL_2FA));
  }

  public Mono<Tuple2<ResponseCookie, String>> finalStep2FA(Api api) {
    var user = api.getUser();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CHANGE_EMAIL_2FA).collectList().then(userRepo
        .toggleEmails(user.getId()).then(bkpCombo.delMatchIfUsed(api)).then(tokenCombo.genSessionTokens(user.getId())));
  }
}
