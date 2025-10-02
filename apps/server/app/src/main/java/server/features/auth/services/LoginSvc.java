package server.features.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.decorators.flow.ErrAPI;
import server.features.auth.paperwork.LoginForm;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class LoginSvc {
  private final UserRepo userRepo;
  private final TkMng tkMng;
  private final TokenRepo tokenRepo;
  private final MyHashMng hashMng;

  public Mono<Tuple2<String, String>> login(LoginForm form) {
    return userRepo.findByEmail(form.getEmail()).flatMap(user ->

    hashMng.argonCheck(user.getPassword(), form.getPassword()).flatMap(resCheck -> {
      if (!resCheck)
        return Mono.<Tuple2<String, String>>error(new ErrAPI("invalid password", 401));

      RecSessionTokensReturnT rec = tkMng.genSessionTokens(user.getId());

      return tokenRepo.deleteByUserIdAndTokenT(user.getId(), TokenT.REFRESH).collectList()
          .doOnNext(ids -> System.out.println("jwe deleted => " + ids.size()))
          .then(tokenRepo.insert(rec.jwe().inst()).thenReturn(Tuples.of(rec.jwe().clientToken(), rec.jwt())));
    })).switchIfEmpty(Mono.error(new ErrAPI("user not found", 404)));
  }
}
