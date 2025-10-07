package server.features.wake_up;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.res_api.ResAPI;
import server.features.wake_up.controllers.GetWakeUpCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/wake-up") @RequiredArgsConstructor
public class WakeUpRouter {

    private final GetWakeUpCtrl getCtrl;

    @GetMapping
    public Mono<ResponseEntity<ResAPI>> wakeUp() {
        return getCtrl.wakeUp();
    }
}
