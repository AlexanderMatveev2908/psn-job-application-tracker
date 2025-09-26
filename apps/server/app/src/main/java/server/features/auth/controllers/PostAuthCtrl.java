package server.features.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@Component
public class PostAuthCtrl {

    public Mono<ResponseEntity<ResAPI<Void>>> register(Api api) {
        return ResAPI.ok200("user registered", null);
    }
}
