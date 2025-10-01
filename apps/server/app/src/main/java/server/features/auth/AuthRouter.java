package server.features.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.auth.controllers.PostAuthCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRouter {
    private final PostAuthCtrl postCtrl;

    @PostMapping("/register")
    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        return postCtrl.register(api);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResAPI>> login(Api api) {
        return postCtrl.login(api);
    }
}
