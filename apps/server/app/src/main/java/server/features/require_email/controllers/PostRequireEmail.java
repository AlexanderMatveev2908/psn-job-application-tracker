package server.features.require_email.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.db.database.DB;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class PostRequireEmail {

  private final DB db;
  private final TokenCombo tokensCombo;

  public Mono<ResponseEntity<ResAPI>> requireMailConfMail(Api api) {
    return db.trxMono((cnt) -> tokensCombo.insertCbcHmacWithMail(api.getUser(), TokenT.CONF_EMAIL))
        .then(new ResAPI(200).msg("email sent").build());

  }

  public Mono<ResponseEntity<ResAPI>> recoverPwd(Api api) {
    return db.trxMono((cnt) -> tokensCombo.insertCbcHmacWithMail(api.getUser(), TokenT.RECOVER_PWD))
        .then(new ResAPI(200).msg("email sent").build());
  }
}
