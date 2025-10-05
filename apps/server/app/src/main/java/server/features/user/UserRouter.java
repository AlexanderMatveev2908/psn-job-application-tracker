package server.features.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.user.controllers.DelUserCtrl;
import server.features.user.controllers.GetUserCtrl;
import server.features.user.controllers.PostUserCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/user") @RequiredArgsConstructor
public class UserRouter {

    private final GetUserCtrl getCtrl;
    private final PostUserCtrl postCtrl;
    private final DelUserCtrl delCtrl;

    @GetMapping("/profile")
    public Mono<ResponseEntity<ResAPI>> getUserProfile(Api api) {
        return getCtrl.getUser(api);
    }

    @PostMapping("/manage-account")
    public Mono<ResponseEntity<ResAPI>> getAccessMngAcc(Api api) {
        return postCtrl.getAccessMngAcc(api);
    }

    @DeleteMapping("/delete-account")
    public Mono<ResponseEntity<ResAPI>> delAccount(Api api) {
        return delCtrl.delAccount(api);
    }
}
