package server.features.auth.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.Api;
import server.lib.security.hash.MyHashMng;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;
import server.paperwork.PwdCheck;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RecoverPwdSvc {
  private final UserRepo userRepo;
  private final TokenCombo tokenCombo;
  private final TokenRepo tokenRepo;
  private final MyHashMng hashMng;

  public Mono<Tuple2<ResponseCookie, String>> mng(Api api) {
    PwdCheck form = api.getMappedData();
    var user = api.getUser();

    return hashMng.argonHash(form.getPassword()).flatMap(hashed -> userRepo.changePwd(user.getId(), hashed))
        .then(tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.RECOVER_PWD).then())
        .then(tokenCombo.genSessionTokens(user));
  }
}
