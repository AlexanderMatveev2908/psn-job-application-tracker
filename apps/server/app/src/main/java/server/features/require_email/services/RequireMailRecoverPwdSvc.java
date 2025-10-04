package server.features.require_email.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.mail.MailSvc;
import server.decorators.flow.Api;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RequireMailRecoverPwdSvc {
  private final TkMng tkMng;
  private final TokenRepo tokenRepo;
  private final MailSvc mailSvc;

  public Mono<Void> mng(Api api) {

    var user = api.getUser();
    var tokenT = TokenT.RECOVER_PWD;

    RecCreateCbcHmacReturnT rec = tkMng.genCbcHmac(tokenT, user.getId());

    return tokenRepo.deleteByUserIdAndTokenT(user.getId(), tokenT)
        .then(tokenRepo.insertWithId(rec.inst()).then(mailSvc.sendRctHtmlMail(tokenT, user, rec.clientToken())));
  }
}
