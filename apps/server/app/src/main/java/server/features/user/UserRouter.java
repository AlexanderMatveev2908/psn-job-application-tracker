package server.features.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.user.controllers.DelUserCtrl;
import server.features.user.controllers.GetUserCtrl;
import server.features.user.controllers.PatchUserCtrl;
import server.features.user.controllers.PostUserCtrl;
import server.router.RouterAPI;

@SuppressFBWarnings({ "EI2" }) @RouterAPI("/api/v1/user") @RequiredArgsConstructor
public class UserRouter {

    private final GetUserCtrl getCtrl;
    private final PostUserCtrl postCtrl;
    private final PatchUserCtrl patchCtrl;
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

    @PatchMapping("/change-pwd")
    public Mono<ResponseEntity<ResAPI>> changePwd(Api api) {
        return patchCtrl.changePwd(api);
    }

    @PatchMapping("/change-email")
    public Mono<ResponseEntity<ResAPI>> changeEmail(Api api) {
        return patchCtrl.changeEmail(api);
    }

    @PatchMapping("/2FA")
    public Mono<ResponseEntity<ResAPI>> setup2FA(Api api) {
        return patchCtrl.setup2FA(api);
    }
}
