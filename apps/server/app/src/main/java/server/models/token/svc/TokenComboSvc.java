package server.models.token.svc;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.conf.mail.MailSvc;
import server.decorators.flow.Api;
import server.lib.security.cookies.MyCookies;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.models.token.MyToken;
import server.models.token.etc.TokenT;
import server.models.user.User;

@Service @RequiredArgsConstructor @SuppressFBWarnings("EI_EXPOSE_REP2")
public class TokenComboSvc {
  private final TokenRepo repo;
  private final TkMng tkMng;
  private final MailSvc mailSvc;
  private final MyCookies ckMng;

  private void logDeleted(int count, TokenT tokenT) {
    System.out.println(String.format("🧹 deleted %d %s tokens", count, tokenT));
  }

  public Mono<MyToken> insertCbcHmac(MyToken token) {
    return repo.delByUserIdAndTokenT(token.getUserId(), token.getTokenType()).collectList().flatMap(ids -> {
      logDeleted(ids.size(), token.getTokenType());
      return repo.insertWithId(token);
    });
  }

  public Mono<MyToken> insertJwe(MyToken token) {
    return repo.delByUserIdAndTokenT(token.getUserId(), TokenT.REFRESH).collectList().flatMap(ids -> {
      logDeleted(ids.size(), token.getTokenType());
      return repo.insert(token);
    });
  }

  public Mono<Void> insertCbcHmacWithMail(Api api, TokenT tokenT) {
    var user = api.getUser();

    var recCbcHmac = tkMng.genCbcHmac(tokenT, user.getId());

    return insertCbcHmac(recCbcHmac.inst()).then(mailSvc.sendRctHtmlMail(tokenT, user, recCbcHmac.clientToken()));
  }

  public Mono<Tuple2<ResponseCookie, String>> genSessionTokens(User user) {
    RecSessionTokensReturnT rec = tkMng.genSessionTokens(user.getId());
    ResponseCookie jweCookie = ckMng.jweCookie(rec.jwe().clientToken());

    return insertJwe(rec.jwe().inst()).thenReturn(Tuples.of(jweCookie, rec.jwt()));

  }
}
