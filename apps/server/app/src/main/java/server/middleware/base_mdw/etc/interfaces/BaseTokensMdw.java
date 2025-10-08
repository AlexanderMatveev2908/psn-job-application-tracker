package server.middleware.base_mdw.etc.interfaces;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.etc.services_mdw.TokenCheckerSvcMdw;
import server.models.token.etc.TokenT;
import server.models.user.User;

public interface BaseTokensMdw {

  TokenCheckerSvcMdw useTokenChecker();

  // ? session tokens
  default Mono<User> checkJwtMandatory(Api api) {
    return useTokenChecker().checkJwt(api, true);
  }

  default Mono<User> checkJwtOptional(Api api) {
    return useTokenChecker().checkJwt(api, false);
  }

  // ? side cbc_hmac tokens non logged user
  default Mono<User> checkQueryCbcHmac(Api api, TokenT tokenT) {
    return useTokenChecker().checkQueryCbcHmac(api, tokenT);
  }

  default Mono<User> checkBodyCbcHmac(Api api, TokenT tokenT) {
    return useTokenChecker().checkBodyCbcHmac(api, tokenT);
  }

  // ? side cbc_hmac tokens logged user
  default Mono<User> checkQueryCbcHmacLogged(Api api, TokenT tokenT) {
    return checkJwtMandatory(api).then(checkQueryCbcHmac(api, tokenT));
  }

  default Mono<User> checkBodyCbcHmacLogged(Api api, TokenT tokenT) {
    return checkJwtMandatory(api).then(checkBodyCbcHmac(api, tokenT));
  }
}
