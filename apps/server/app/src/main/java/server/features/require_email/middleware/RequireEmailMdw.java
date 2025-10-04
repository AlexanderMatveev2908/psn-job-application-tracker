// package server.features.require_email.middleware;

// import org.springframework.stereotype.Component;
// import org.springframework.web.server.WebFilterChain;

// import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
// import lombok.RequiredArgsConstructor;
// import reactor.core.publisher.Mono;
// import server.decorators.flow.Api;
// import server.middleware.BaseMdw;
// import server.paperwork.EmailCheck;

// @SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
// public class RequireEmailMdw extends BaseMdw {

// @Override
// public Mono<Void> handle(Api api, WebFilterChain chain) {
// return isTarget(api, chain, "/require-email/confirm-email", () -> {
// return limitAndRef(api).flatMap(body->{
// EmailCheck form = EmailCheck.fromBody(body);

// });
// });
// }
// }
