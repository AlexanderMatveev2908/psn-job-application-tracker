package server.middleware.security;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecAad;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenSvc;
import server.models.user.User;
import server.models.user.svc.UserSvc;

@SuppressFBWarnings({ "EI2" }) @Service @RequiredArgsConstructor
public class CheckTokenMdw {

  private final UserSvc userSvc;
  private final TkMng tkMng;
  private final TokenSvc tokenSvc;
  private final MyHashMng hashMng;

  public Mono<User> checkJwt(Api api, boolean throwIfMiss) {
    String jwt = api.getJwt();
    String jwe = api.getJwe();

    if (jwt.isBlank()) {
      if (!throwIfMiss && jwe.isBlank())
        return Mono.empty();
      else
        return Mono.error(new ErrAPI("jwt_not_provided", 401));
    }

    MyTkPayload payload = tkMng.checkJwt(jwt);

    return userSvc.findById(payload.userId()).switchIfEmpty(Mono.error(new ErrAPI("jwt_invalid", 401))).map(user -> {
      api.setUserAttr(user);
      return user;
    });
  }

  public Mono<User> checkQueryCbcHmac(Api api, TokenT tokenT) {
    String token = api.getQueryCbcHmac();

    if (token.isBlank())
      return Mono.error(new ErrAPI("cbc_hmac_not_provided", 401));

    return checkCbcHmac(api, tokenT, token);
  }

  public Mono<User> checkBodyCbcHmac(Api api, TokenT tokenT) {
    return api.getBdCbcHmac().switchIfEmpty(Mono.error(new ErrAPI("cbc_hmac_not_provided", 401)))
        .flatMap(token -> checkCbcHmac(api, tokenT, token));
  }

  private Mono<User> checkCbcHmac(Api api, TokenT tokenT, String token) {

    try {
      MyTkPayload payload = tkMng.checkCbcHmac(token);

      if (!RecAad.fromToken(token).getTokenT().equals(tokenT))
        return Mono.error(new ErrAPI("cbc_hmac_wrong_type", 401));

      return tokenSvc.findByUserIdTypeHash(payload.userId(), tokenT, hashMng.hmacHash(token))
          .switchIfEmpty(Mono.error(new ErrAPI("cbc_hmac_not_found", 401)))
          .flatMap(dbToken -> userSvc.findById(dbToken.getUserId())
              .switchIfEmpty(Mono.error(new ErrAPI("cbc_hmac_invalid", 401))).map(dbUser -> {
                api.setUserAttr(dbUser);
                return dbUser;
              }));
    } catch (Exception err) {
      Map<String, Object> data = null;

      if (err instanceof ErrAPI errInst)
        data = errInst.getData();

      return (data != null && data.get("idCbcHmacRm") instanceof UUID tokenId)
          ? tokenSvc.delById(tokenId).then(Mono.error(new ErrAPI(err.getMessage(), 401)))
          : Mono.error(err);
    }
  }
}
