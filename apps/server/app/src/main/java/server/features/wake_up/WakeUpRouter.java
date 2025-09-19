package server.features.wake_up;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

import server.features.wake_up.controllers.GetCtrl;
import server.router.RootApi;

@RootApi
public class WakeUpRouter {

    private final GetCtrl getCtrl;

    public WakeUpRouter(GetCtrl getCtrl) {
        this.getCtrl = getCtrl;
    }

    @GetMapping("/wake-up")
    public Map<String, Object> wakeUpGet() {
        return getCtrl.wakeUp();
    }

}
