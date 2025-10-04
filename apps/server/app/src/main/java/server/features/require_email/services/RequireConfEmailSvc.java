package server.features.require_email.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenComboSvc;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RequireConfEmailSvc {
  private final TokenComboSvc tokenCombo;

  public Mono<Void> mng(Api api) {
    return tokenCombo.insertCbcHmacWithMail(api, TokenT.CONF_EMAIL);
  }
}
