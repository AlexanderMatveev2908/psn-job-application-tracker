package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.applications.JobAppl;
import server.models.applications.svc.JobCombo;
import server.paperwork.job_application.post.JobApplForm;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class PutJobApplMdw extends BaseMdw {

  private final JobCombo jobCombo;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return matchPath(api, chain, "/job-applications", HttpMethod.PUT, () -> {
      return limit(api, 15, 15).then(withPathId(api).flatMap(jobId -> checkMultipartForm(api, JobApplForm.class)
          .then(jobCombo.existsAndBelongs(api, jobId).flatMap(dbSaved -> {
            api.setMappedDataAttr(JobAppl.fromAttrApi(api));
            return chain.filter(api);
          }))));
    });
  }
}