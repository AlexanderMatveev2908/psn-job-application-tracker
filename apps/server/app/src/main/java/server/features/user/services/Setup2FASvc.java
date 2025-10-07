package server.features.user.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.lib.dev.MyLog;
import server.lib.security.tfa.My2FA;
import server.lib.security.tfa.etc.Rec2FA;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class Setup2FASvc {
  private final My2FA tfa;
  private final UserRepo userRepo;
  private final BkpCodesRepo codesRepo;

  public Mono<Rec2FA> mng(Api api) {
    var user = api.getUser();

    return tfa.setup2FA(user).flatMap(rec -> userRepo.setTotpSecret(rec.recTOTP().encrypted(), user.getId())
        .thenMany(Flux.fromIterable(rec.recBkpCodes().hashed()).flatMap(code -> codesRepo.insert(user.getId(), code)))
        .collectList().doOnNext(saved -> MyLog.log("bkp codes inserted => " + saved.size())).thenReturn(rec));
  }

}
