package server.models.token.svc;

import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.conf.mail.MailSvc;
import server.lib.security.cookies.MyCookies;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.MyToken;
import server.models.token.etc.TokenT;
import server.models.user.User;

@Service @RequiredArgsConstructor @SuppressFBWarnings("EI_EXPOSE_REP2")
public class TokenCombo {
  private final TokenRepo repo;
  private final TkMng tkMng;
  private final MailSvc mailSvc;
  private final MyCookies ckMng;

  private Mono<MyToken> insertCbcHmac(MyToken token) {
    return repo.delByUserIdAndTokenT(token.getUserId(), token.getTokenType()).collectList().flatMap(ids -> {
      return repo.insertWithId(token);
    });
  }

  public Mono<String> insertCbcHmac(UUID userId, TokenT tokenT) {
    RecCreateCbcHmacReturnT rec = tkMng.genCbcHmac(tokenT, userId);
    return insertCbcHmac(rec.inst()).thenReturn(rec.clientToken());

  }

  public Mono<MyToken> insertJwe(MyToken token) {
    return repo.delByUserIdAndTokenT(token.getUserId(), TokenT.REFRESH).collectList().flatMap(ids -> {
      return repo.insert(token);
    });
  }

  public Mono<Void> insertCbcHmacWithMail(User user, String newEmail, TokenT tokenT) {

    var recCbcHmac = tkMng.genCbcHmac(tokenT, user.getId());

    return insertCbcHmac(recCbcHmac.inst())
        .then(mailSvc.sendRctHtmlMail(tokenT, user, newEmail, recCbcHmac.clientToken()));
  }

  public Mono<Void> insertCbcHmacWithMail(User user, TokenT tokenT) {
    return insertCbcHmacWithMail(user, null, tokenT);
  }

  public Mono<Tuple2<ResponseCookie, String>> genSessionTokens(UUID userId) {

    RecSessionTokensReturnT rec = tkMng.genSessionTokens(userId);
    ResponseCookie jweCookie = ckMng.jweCookie(rec.jwe().clientToken());

    return insertJwe(rec.jwe().inst()).thenReturn(Tuples.of(jweCookie, rec.jwt()));

  }
}
