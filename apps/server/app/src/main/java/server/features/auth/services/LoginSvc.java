package server.features.auth.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.ErrAPI;
import server.features.auth.paperwork.LoginForm;
import server.lib.security.hash.MyHashMng;
import server.models.token.svc.TokenComboSvc;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class LoginSvc {
  private final UserRepo userRepo;
  private final MyHashMng hashMng;
  private final TokenComboSvc tokenComboSvc;

  public Mono<Tuple2<ResponseCookie, String>> login(LoginForm form) {
    return userRepo.findByEmail(form.getEmail()).flatMap(user ->

    hashMng.argonCheck(user.getPassword(), form.getPassword()).flatMap(resCheck -> {
      if (!resCheck)
        return Mono.error(new ErrAPI("invalid password", 401));

      return tokenComboSvc.genSessionTokens(user);
    })).switchIfEmpty(Mono.error(new ErrAPI("user not found", 404)));
  }
}
