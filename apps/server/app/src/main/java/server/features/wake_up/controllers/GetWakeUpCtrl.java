package server.features.wake_up.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.ResAPI;

@Component
public class GetWakeUpCtrl {

    public Mono<ResponseEntity<ResAPI>> wakeUp() {

        return new ResAPI(200).msg("ops I did not listen the alarm ⏰").build();
    }
}
