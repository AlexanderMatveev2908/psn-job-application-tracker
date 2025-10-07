package server.features.user.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.user.svc.UserRepo;
import server.paperwork.EmailForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class ChangeMailSvc {
  private final TokenCombo tokenCombo;
  private final UserRepo userRepo;

  public Mono<Void> mng(Api api) {

    var user = api.getUser();
    EmailForm mailForm = api.getMappedData();

    return userRepo.setTmpEmail(user.getId(), mailForm.getEmail())
        .then(tokenCombo.insertCbcHmacWithMail(api.getUser(), mailForm.getEmail(), TokenT.CHANGE_EMAIL));
  }
}
