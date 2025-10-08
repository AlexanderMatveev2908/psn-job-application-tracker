package server.middleware.base_mdw.etc.interfaces;

import java.util.Map;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.etc.services_mdw.UserPwdCheckerSvcMdw;

public interface BasePwdMdw {

  UserPwdCheckerSvcMdw useUserPwdChecker();

  Mono<Map<String, Object>> grabBody(Api api);

  default Mono<Void> checkUserPwdToMatch(Api api, String plainText) {
    return useUserPwdChecker().checkUserPwd(api, plainText, true);
  }

  default Mono<Void> checkUserPwdToNotMatch(Api api, String plainTxt) {
    return useUserPwdChecker().checkUserPwd(api, plainTxt, false);
  }

}
