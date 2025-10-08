package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.BaseMdw;
import server.paperwork.job_application.JobApplForm;

@Component @RequiredArgsConstructor
public class CreateJobApplMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/job-applications", HttpMethod.POST, () -> {
      return limitWithFormData(api, 15, 15).flatMap(body -> {
        var form = Prs.fromMapToT(body, JobApplForm.class);
        return checkForm(api, form).then(chain.filter(api));
      });
    });
  }
}
