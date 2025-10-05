package server.features.user.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.db.database.DB;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostUserCtrl {

  private final DB db;
  private final TokenCombo tkCombo;
  private final TkMng tkMng;

  public Mono<ResponseEntity<ResAPI>> getAccessMngAcc(Api api) {

    var user = api.getUser();
    RecCreateCbcHmacReturnT rec = tkMng.genCbcHmac(TokenT.MANAGE_ACC, user.getId());

    return db.trxMono(cnt -> tkCombo.insertCbcHmac(rec.inst()))
        .then(new ResAPI(200).msg("ok").data(Map.of("cbcHmacToken", rec.clientToken())).build());
  }
}