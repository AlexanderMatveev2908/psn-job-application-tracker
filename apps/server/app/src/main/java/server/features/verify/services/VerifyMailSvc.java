package server.features.verify.services;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.decorators.flow.Api;
import server.lib.security.cookies.MyCookies;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyMailSvc {

  private final UserRepo userRepo;
  private final TokenRepo tokenRepo;
  private final MyCookies cookiesMng;
  private final TkMng tkMng;

  public Mono<Tuple2<ResponseCookie, String>> mng(Api api) {
    var user = api.getUser();

    RecSessionTokensReturnT recSession = tkMng.genSessionTokens(user.getId());
    ResponseCookie jweCookie = cookiesMng.jweCookie(recSession.jwe().clientToken());

    return userRepo.verifyUser(user.getId())
        .then(Mono.when(tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.CONF_EMAIL).then(),
            tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.REFRESH).then()))
        .then(tokenRepo.insert(recSession.jwe().inst())).thenReturn(Tuples.of(jweCookie, recSession.jwt()));
  }
}
