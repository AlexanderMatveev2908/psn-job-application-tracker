package server.features.auth.services;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.security.hash.DbHash;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.models.token.MyToken;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RefreshSvc {
  private final TkMng tkMng;
  private final TokenRepo tokenRepo;
  private final DbHash dbHash;

  public Mono<MyToken> refresh(Api api) {

    String jwe = api.getJwe();
    MyTkPayload payload = tkMng.checkJwe(jwe);

    return tokenRepo.findByUserIdAndTokenT(payload.userId(), TokenT.REFRESH).flatMap(dbToken -> {

      if (!dbHash.check(dbToken.getHashed(), jwe))
        return Mono.error(new ErrAPI("jwe_invalid", 401));
      if (dbToken.getExp() < Instant.now().getEpochSecond())
        return tokenRepo.deleteByUserIdAndTokenT(dbToken.getUserId(), TokenT.REFRESH)
            .then(Mono.error(new ErrAPI("jwe_expired", 401)));

      return Mono.just(dbToken);
    });

  }
}
