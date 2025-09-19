package server.features.wake_up;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import server.decorators.flow.ResAPI;
import server.features.wake_up.controllers.GetWakeUpCtrl;
import server.router.RootApi;

@RootApi
public class WakeUpRouter {

    private final GetWakeUpCtrl getCtrl;

    public WakeUpRouter(GetWakeUpCtrl getCtrl) {
        this.getCtrl = getCtrl;
    }

    @GetMapping("/wake-up")
    public ResponseEntity<ResAPI<Void>> wakeUp() {
        return getCtrl.wakeUp();
    }

}
