package server.features.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@Component
public class GetTestCtrl {

    public Mono<ResponseEntity<ResAPI<Void>>> getLimited(Api api) {
        return ResAPI.ok200("get request limited 🚦", null);
    }

    public Mono<ResponseEntity<ResAPI<Object>>> getTest(Api api) {
        return ResAPI.ok200("get request received 👻", null);
    }
}
