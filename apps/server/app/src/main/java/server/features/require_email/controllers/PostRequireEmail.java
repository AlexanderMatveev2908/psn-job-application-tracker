package server.features.require_email.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.require_email.services.RequireConfEmailSvc;
import server.features.require_email.services.RequireMailRecoverPwdSvc;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class PostRequireEmail {

  private final RequireConfEmailSvc confMailSvc;
  private final RequireMailRecoverPwdSvc recoverPwdSvc;

  public Mono<ResponseEntity<ResAPI>> requireMailConfMail(Api api) {
    return confMailSvc.mng(api).then(new ResAPI(200).msg("email sent").build());
  }

  public Mono<ResponseEntity<ResAPI>> recoverPwd(Api api) {
    return recoverPwdSvc.mng(api).then(new ResAPI(200).msg("email sent").build());
  }
}
