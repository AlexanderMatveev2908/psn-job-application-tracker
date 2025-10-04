package server.features.require_email.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.mail.MailSvc;
import server.decorators.flow.Api;
import server.lib.security.mng_tokens.TkMng;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RequireConfEmailSvc {
  private final TokenRepo tokenRepo;
  private final TkMng tkMng;
  private final MailSvc mailSvc;

  public Mono<Void> mng(Api api) {
    var user = api.getUser();

    var recCbcHmac = tkMng.genCbcHmac(TokenT.CONF_EMAIL, user.getId());

    return tokenRepo.deleteByUserIdAndTokenT(user.getId(), TokenT.CONF_EMAIL)
        .then(tokenRepo.insertWithId(recCbcHmac.inst())
            .then(mailSvc.sendRctHtmlMail(TokenT.CONF_EMAIL, user, recCbcHmac.clientToken())));
  }
}
