package server.features.user.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.applications.svc.JobApplRepo;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class DelAccSvc {

  private final TokenRepo tokenRepo;
  private final UserRepo userRepo;
  private final BkpCodesRepo bkpCodeRepo;
  private final JobApplRepo jobApplRepo;

  public Mono<Void> mng(Api api) {
    var userId = api.getUser().getId();

    return jobApplRepo.delByUserId(userId).collectList().then(bkpCodeRepo.delByUserId(userId).collectList())
        .then(tokenRepo.delByUserId(userId).collectList()).then(userRepo.deleteById(userId));
  }
}
