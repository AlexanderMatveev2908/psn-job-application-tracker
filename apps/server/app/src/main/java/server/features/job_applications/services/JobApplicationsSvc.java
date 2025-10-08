package server.features.job_applications.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class JobApplicationsSvc {
  public Mono<Void> example() {

    return Mono.empty();
  }
}
