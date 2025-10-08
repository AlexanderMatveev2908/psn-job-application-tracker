package server.features.auth.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.api.Api;
import server.lib.security.hash.MyHashMng;
import server.models.backup_code.svc.BkpCombo;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.svc.UserRepo;
import server.paperwork.user_validation.PwdForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RecoverPwdSvc {
  private final UserRepo userRepo;
  private final TokenCombo tokenCombo;
  private final TokenRepo tokenRepo;
  private final MyHashMng hashMng;
  private final BkpCombo bkpCombo;

  private Mono<Tuple2<ResponseCookie, String>> changePwdComn(User user, String newPwd) {
    return hashMng.argonHash(newPwd).flatMap(hashed -> userRepo.changePwd(user.getId(), hashed))
        .then(tokenCombo.genSessionTokens(user.getId()));
  }

  public Mono<Tuple2<ResponseCookie, String>> simpleFlow(Api api) {
    PwdForm form = api.getMappedData();
    var user = api.getUser();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.RECOVER_PWD).then()
        .then(changePwdComn(user, form.getPassword()));
  }

  public Mono<Tuple2<ResponseCookie, String>> flow2FA(Api api) {
    PwdForm form = api.getMappedData();
    var user = api.getUser();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.RECOVER_PWD_2FA).collectList()
        .then(bkpCombo.delMatchIfUsed(api).then(changePwdComn(user, form.getPassword())));
  }
}
