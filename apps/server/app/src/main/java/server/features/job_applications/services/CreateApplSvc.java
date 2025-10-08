package server.features.job_applications.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.applications.JobAppl;
import server.models.applications.svc.JobApplRepo;
import server.paperwork.job_application.JobApplForm;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class CreateApplSvc {
  private final JobApplRepo jobRepo;

  public Mono<JobAppl> mng(Api api) {
    var user = api.getUser();
    JobApplForm form = api.getMappedData();

    return jobRepo.insert(JobAppl.fromUserForm(user.getId(), form)).doOnError(err -> err.printStackTrace());
  }
}
