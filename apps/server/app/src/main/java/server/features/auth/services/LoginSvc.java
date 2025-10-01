package server.features.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.features.auth.paperwork.LoginForm;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class LoginSvc {
  private final UserRepo userRepo;

  public Mono<User> login(LoginForm form) {
    return userRepo.findByEmail(form.getEmail()).switchIfEmpty(Mono.error(new ErrAPI("user not found", 404)));
  }
}
