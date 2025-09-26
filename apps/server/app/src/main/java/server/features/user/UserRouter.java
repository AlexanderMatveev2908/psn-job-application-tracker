package server.features.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.user.controllers.GetUserCtrl;
import server.router.RouterAPI;

@RouterAPI
@RequiredArgsConstructor
public class UserRouter {

    private final GetUserCtrl getCtrl;

    @GetMapping("/user/profile")
    public Mono<ResponseEntity<ResAPI<Void>>> getUserProfile(Api api) {
        return getCtrl.getUser(api);
    }
}
