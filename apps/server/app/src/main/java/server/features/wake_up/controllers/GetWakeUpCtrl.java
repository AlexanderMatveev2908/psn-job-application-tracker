package server.features.wake_up.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.ResAPI;

@Component
public class GetWakeUpCtrl {

    public Mono<ResponseEntity<ResAPI<Object>>> wakeUp() {

        return ResAPI.ok200("Ops I did not listen the alarm ⏰", null);
    }
}
