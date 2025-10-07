package server.middleware.security;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.ShapeCheck;
import server.lib.dev.MyLog;
import server.lib.security.hash.MyHashMng;
import server.lib.security.tfa.My2FA;
import server.models.backup_code.etc.RecInfoBkp;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.user.User;
import server.paperwork.tfa.TFAForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2", "REC_CATCH_EXCEPTION" })
public class Check2FAMdw {
  private final BkpCodesRepo bkpCodesRepo;
  private final My2FA tfa;
  private final MyHashMng hashMng;

  public Mono<Void> check2FA(Api api, TFAForm form) {
    var user = api.getUser();

    return ShapeCheck.isStr(form.getTotpCode()) ? checkTotpCode(user, form.getTotpInt())
        : checkBkpCode(user, form.getBackupCode()).flatMap(rec -> Mono.fromRunnable(() -> api.setInfoBkp(rec)));
  }

  private Mono<Void> checkTotpCode(User user, Integer totpCode) {
    var resCheck = tfa.checkTotp(user.getTotpSecret(), totpCode);

    if (!resCheck)
      return Mono.error(new ErrAPI("totp_code_invalid", 401));

    return Mono.empty();
  }

  private Mono<RecInfoBkp> checkBkpCode(User user, String backupCode) {

    return bkpCodesRepo.findByUserId(user.getId()).collectList().flatMap(codes -> {

      if (codes.isEmpty())
        return Mono.error(new ErrAPI("user has no backup codes left", 401));

      return Flux.fromIterable(codes)
          .flatMap(dbCode -> hashMng.argonCheck(dbCode.getCode(), backupCode)
              .flatMap(resCheck -> resCheck ? Mono.just(dbCode) : Mono.empty()))
          .collectList().flatMap(matches -> matches.isEmpty() ? Mono.error(new ErrAPI("backup_code_invalid", 401))
              : Mono.just(matches.get(0)))
          .flatMap(m -> {

            return bkpCodesRepo.delById(m.getId()).collectList().doOnNext(ids -> {
              MyLog.log("🧹 deleted bkp => " + ids.get(0));
            }).thenReturn(new RecInfoBkp(m, codes.size()));
          });

    });
  }
}
