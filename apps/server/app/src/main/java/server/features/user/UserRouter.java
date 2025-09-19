package server.features.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import server.decorators.flow.ReqAPI;
import server.features.user.controllers.GetUserCtrl;
import server.router.RootApi;

@RootApi
public class UserRouter {
    private final GetUserCtrl getCtrl;

    public UserRouter(GetUserCtrl getCtrl) {
        this.getCtrl = getCtrl;
    }

    @GetMapping("/user/profile")
    public ResponseEntity<Void> postMsg(ReqAPI req) {
        return getCtrl.getUser(req);
    }

}
