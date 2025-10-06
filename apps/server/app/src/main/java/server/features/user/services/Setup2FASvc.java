package server.features.user.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.lib.security.tfa.My2FA;
import server.lib.security.tfa.etc.Rec2FA;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class Setup2FASvc {
  private final My2FA tfa;

  public Mono<Rec2FA> mng(Api api) {

    return tfa.setup2FA(api);
  }

}
