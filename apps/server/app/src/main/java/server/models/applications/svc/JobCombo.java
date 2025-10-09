package server.models.applications.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.models.applications.JobAppl;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class JobCombo {
  private final JobApplRepo jobRepo;

  public Mono<JobAppl> existsAndBelongs(Api api, UUID jobId) {

    return jobRepo.findById(jobId).switchIfEmpty(Mono.error(new ErrAPI("job application not found", 404)))
        .flatMap(dbSaved -> {
          if (!api.getUser().getId().equals(dbSaved.getUserId()))
            return Mono.error(new ErrAPI("forbidden", 403));

          api.setCurrApplication(dbSaved);

          return Mono.just(dbSaved);
        });
  }
}
