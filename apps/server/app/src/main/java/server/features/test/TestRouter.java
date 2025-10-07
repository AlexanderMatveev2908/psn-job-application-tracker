package server.features.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.test.controllers.GetTestCtrl;
import server.features.test.controllers.PostTestCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/test") @RequiredArgsConstructor
public class TestRouter {

    private final PostTestCtrl postCtrl;
    private final GetTestCtrl getCtrl;

    @GetMapping("/limited")
    public Mono<ResponseEntity<ResAPI>> getLimited(Api exc) {
        return getCtrl.getLimited(exc);
    }

    @GetMapping("/protected")
    public Mono<ResponseEntity<ResAPI>> getProtectedData(Api api) {
        return getCtrl.getProtectedData(api);
    }

    @PostMapping("/user")
    public Mono<ResponseEntity<ResAPI>> getUserTest(Api api) {
        return postCtrl.getUserTest(api);
    }

    @GetMapping
    public Mono<ResponseEntity<ResAPI>> getTest(Api api) {
        return getCtrl.getTest(api);
    }

    @PostMapping
    public Mono<ResponseEntity<ResAPI>> postMsg(Api api) {
        return postCtrl.postMsg(api);
    }

    @PostMapping("/form-data")
    public Mono<ResponseEntity<ResAPI>> postFormData(Api api) {
        return postCtrl.postFormData(api);
    }
}
