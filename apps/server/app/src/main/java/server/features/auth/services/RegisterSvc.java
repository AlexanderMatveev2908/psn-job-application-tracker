package server.features.auth.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.ErrAPI;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RegisterSvc {
  private final UserRepo userRepo;
  private final TokenCombo tokenComboSvc;

  public Mono<Tuple2<ResponseCookie, String>> register(User us) {
    return userRepo.findByEmail(us.getEmail())
        .flatMap(existing -> Mono
            .<Tuple2<ResponseCookie, String>>error(new ErrAPI("an account with this email already exists", 409)))
        .switchIfEmpty(userRepo.insert(us).flatMap(dbUser -> {

          return tokenComboSvc.insertCbcHmacWithMail(dbUser, TokenT.CONF_EMAIL)
              .then(tokenComboSvc.genSessionTokens(dbUser));
        }));
  }

}
