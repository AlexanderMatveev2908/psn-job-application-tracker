package server.features.wake_up.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import server.decorators.flow.ResAPI;

@Component
public class GetWakeUpCtrl {
    public ResponseEntity<ResAPI<Void>> wakeUp() {
        return ResAPI.ok200("Ops I did not listen the alarm ⏰", null);
    }
}