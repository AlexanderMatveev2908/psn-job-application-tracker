package server.features.wake_up;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import server.features.wake_up.controllers.GetCtrl;
import server.features.wake_up.controllers.PostCtrl;
import server.router.RootApi;

@RootApi
public class WakeUpRouter {

    private final PostCtrl postCtrl;
    private final GetCtrl getCtrl;

    public WakeUpRouter(PostCtrl postCtrl, GetCtrl getCtrl) {
        this.postCtrl = postCtrl;
        this.getCtrl = getCtrl;
    }

    @GetMapping("/wake-up")
    public Map<String, Object> wakeUpGet() {
        return getCtrl.wakeUp();
    }

    @PostMapping("/wake-up")
    public Map<String, Object> wakeUpPost(HttpServletRequest req) {
        return postCtrl.wakeUp(req);
    }
}
