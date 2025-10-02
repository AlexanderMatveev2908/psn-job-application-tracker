package server.features.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RefreshSvc {
  private final TkMng tkMng;
  private final TokenRepo tokenRepo;
  private final MyHashMng hashMng;

  public Mono<String> refresh(Api api) {

    String jwe = api.getJwe();

    if (jwe.isBlank())
      throw new ErrAPI("jwe_not_provided", 401);

    MyTkPayload payload = tkMng.checkJwe(jwe);

    String recomputed = hashMng.hmacHash(jwe);

    return tokenRepo.findByHash(payload.userId(), TokenT.REFRESH, recomputed).flatMap(dbToken -> {
      String freshJwt = tkMng.genJwt(payload.userId());
      return Mono.just(freshJwt);
    }).switchIfEmpty(Mono.error(new ErrAPI("jwe_not_found", 401)));
  }
}
