package server.features.auth.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.features.auth.services.RefreshSvc;
import server.lib.security.cookies.MyCookies;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class GetAuthCtrl {

  private final RefreshSvc refreshSvc;
  private final TokenSvc tokenSvc;
  private final MyCookies cookieMng;

  public Mono<ResponseEntity<ResAPI>> refresh(Api api) {

    return refreshSvc.refresh(api).flatMap(jwt -> new ResAPI(200).data(Map.of("accessToken", jwt)).build())
        .onErrorResume(err -> {
          int status = 500;
          Map<String, Object> data = null;

          if (err instanceof ErrAPI instErr) {
            status = instErr.getStatus();
            data = instErr.getData();
          }

          ResAPI res = new ResAPI(status).msg(err.getMessage()).delCookie(cookieMng.delJweCookie());

          if (data != null && data.get("argDeleteJwe") instanceof UUID userId)
            return tokenSvc.deleteByUserIdAndTokenT(userId, TokenT.REFRESH).then(res.build());

          return res.build();

        });
  }
}
