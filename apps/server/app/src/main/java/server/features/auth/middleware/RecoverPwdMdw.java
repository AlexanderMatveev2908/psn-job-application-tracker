package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.security.hash.MyHashMng;
import server.middleware.BaseMdw;
import server.models.token.etc.TokenT;
import server.paperwork.PwdCheck;

@Component @RequiredArgsConstructor
public class RecoverPwdMdw extends BaseMdw {

  private final MyHashMng hashMng;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/auth/recover-pwd", () -> {
      return limitCheckBodyCbcHmacRefBody(api, TokenT.RECOVER_PWD).flatMap(body -> {
        var form = PwdCheck.fromBody(body);
        var user = api.getUser();

        return checkForm(api, form).then(hashMng.argonCheck(user.getPassword(), form.getPassword())
            .flatMap(resCheck -> resCheck ? Mono.error(new ErrAPI("new password must be different from old one", 401))
                : chain.filter(api)));
      });
    });
  }
}