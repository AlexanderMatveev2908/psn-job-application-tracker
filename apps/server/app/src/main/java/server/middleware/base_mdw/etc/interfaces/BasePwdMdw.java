package server.middleware.base_mdw.etc.interfaces;

import java.util.Map;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.etc.services_mdw.UserPwdCheckerSvcMdw;
import server.paperwork.user_validation.PwdForm;

public interface BasePwdMdw {

  UserPwdCheckerSvcMdw useUserPwdChecker();

  Mono<Map<String, Object>> grabBody(Api api);

  <T> Mono<Void> checkForm(Api api, T form);

  default Mono<Void> checkUserPwdToMatch(Api api, String plainText) {
    return useUserPwdChecker().checkUserPwd(api, plainText, true);
  }

  default Mono<Void> checkUserPwdToNotMatch(Api api, String plainTxt) {
    return useUserPwdChecker().checkUserPwd(api, plainTxt, false);
  }

  default Mono<String> checkPwdReg(Api api) {
    return grabBody(api).flatMap(body -> {
      var form = PwdForm.fromBody(body);
      return checkForm(api, form).thenReturn(form.getPassword());
    });
  }

}
