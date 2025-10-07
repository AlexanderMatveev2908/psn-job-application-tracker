package server.middleware.security;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.ShapeCheck;
import server.lib.security.tfa.My2FA;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.user.User;
import server.paperwork.tfa.TFAForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2", "REC_CATCH_EXCEPTION" })
public class Check2FAMdw {
  private final BkpCodesRepo bkpCodesRepo;
  private final My2FA tfa;

  public Mono<Void> check2FA(User user, TFAForm form) {

    return ShapeCheck.isStr(form.getTotpCode()) ? checkTotpCode(user, form.getTotpCode())
        : checkBkpCode(user, form.getBackupCode());
  }

  private Mono<Void> checkTotpCode(User user, String totpCode) {
    if (!Reg.isTotpCode(totpCode))
      return Mono.error(new ErrAPI("totp_code_invalid", 401));

    var resCheck = tfa.checkTotp(user.getTotpSecret(), totpCode);

    if (!resCheck)
      return Mono.error(new ErrAPI("totp_code_invalid", 401));

    return Mono.empty();
  }

  private Mono<Void> checkBkpCode(User user, String backupCode) {
    return Mono.empty();
  }
}
