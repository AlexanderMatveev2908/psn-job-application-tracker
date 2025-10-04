package server.features.verify.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class VerifyRecoverPwdSvc {
  public Mono<Void> mng() {

    return Mono.empty();
  }
}
