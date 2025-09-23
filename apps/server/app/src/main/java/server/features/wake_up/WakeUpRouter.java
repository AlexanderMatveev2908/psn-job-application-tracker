package server.features.wake_up;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import server.decorators.flow.ResAPI;
import server.features.wake_up.controllers.GetWakeUpCtrl;
import server.router.RouterAPI;

@RestController
@RouterAPI
public class WakeUpRouter {

    private final GetWakeUpCtrl getCtrl;

    public WakeUpRouter(GetWakeUpCtrl getCtrl) {
        this.getCtrl = getCtrl;
    }

    @GetMapping("/wake-up")
    public Mono<ResponseEntity<ResAPI<Object>>> wakeUp() {
        return getCtrl.wakeUp();
    }
}
