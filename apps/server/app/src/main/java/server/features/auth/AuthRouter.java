package server.features.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.auth.controllers.GetAuthCtrl;
import server.features.auth.controllers.PatchAuthCtrl;
import server.features.auth.controllers.PostAuthCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/auth") @RequiredArgsConstructor
public class AuthRouter {
    private final PostAuthCtrl postCtrl;
    private final GetAuthCtrl getCtrl;
    private final PatchAuthCtrl patchCtrl;

    @PostMapping("/register")
    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        return postCtrl.register(api);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResAPI>> login(Api api) {
        return postCtrl.login(api);
    }

    @PostMapping("/login-2FA")
    public Mono<ResponseEntity<ResAPI>> login2FA(Api api) {
        return postCtrl.login2FA(api);
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<ResAPI>> logout(Api api) {
        return postCtrl.logout(api);
    }

    @GetMapping("/refresh")
    public Mono<ResponseEntity<ResAPI>> refresh(Api api) {
        return getCtrl.refresh(api);
    }

    @PatchMapping("/recover-pwd")
    public Mono<ResponseEntity<ResAPI>> recoverPwd(Api api) {
        return patchCtrl.recoverPwd(api);
    }

    @PatchMapping("/recover-pwd-2FA")
    public Mono<ResponseEntity<ResAPI>> recoverPwd2F(Api api) {
        return patchCtrl.recoverPwd2FA(api);
    }
}
