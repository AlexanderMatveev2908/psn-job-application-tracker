package server.features.verify.controllers;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.lib.security.cookies.MyCookies;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Component @RequiredArgsConstructor
public class GetVerifyCtrl {

  private final UserRepo userRepo;
  private final TokenRepo tokenRepo;
  private final MyCookies cookiesMng;
  private final TkMng tkMng;

  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    User user = api.getUser();

    RecSessionTokensReturnT recSession = tkMng.genSessionTokens(user.getId());
    ResponseCookie jweCookie = cookiesMng.jweCookie(recSession.jwe().clientToken());

    return userRepo.verifyUser(user.getId())
        .then(Mono.when(tokenRepo.deleteByUserIdAndTokenT(user.getId(), TokenT.CONF_EMAIL).then(),
            tokenRepo.deleteByUserIdAndTokenT(user.getId(), TokenT.REFRESH).then()))
        .then(tokenRepo.insert(recSession.jwe().inst())).then(new ResAPI(200).msg("user verified").cookie(jweCookie)
            .data(Map.of("accessToken", recSession.jwt())).build());
  }

}
