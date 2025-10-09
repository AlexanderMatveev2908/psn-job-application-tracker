package server.features.job_applications.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.applications.JobAppl;
import server.models.applications.svc.JobApplRepo;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class DelJobApplSvc {
  private final JobApplRepo jobRepo;

  public Mono<Void> mng(Api api) {

    JobAppl existing = api.getCurrApplication();

    return jobRepo.deleteById(existing.getId());
  }
}
