package server.features.user.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.lib.security.hash.MyHashMng;
import server.models.user.svc.UserRepo;
import server.paperwork.PwdForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class ChangePwdSvc {
  private final UserRepo useRepo;
  private final MyHashMng hashMng;

  public Mono<Void> mng(Api api) {
    var user = api.getUser();
    PwdForm form = api.getMappedData();

    return hashMng.argonHash(form.getPassword()).flatMap(hashed -> useRepo.changePwd(user.getId(), hashed)).then();

  }
}
