package server.features.verify.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.backup_code.svc.BkpCombo;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyRecoverPwd2FASvc {
  private final TokenRepo tokenRepo;
  private final BkpCombo bkpCombo;
  private final TokenCombo tokenCombo;

  public Mono<String> mng(Api api) {
    var user = api.getUser();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.RECOVER_PWD).collectList()
        .then(bkpCombo.delMatchIfUsed(api)).then(tokenCombo.insertCbcHmac(user.getId(), TokenT.RECOVER_PWD_2FA));
  }
}
