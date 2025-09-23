package server.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@Component
public class GetUserCtrl {

    public Mono<ResponseEntity<ResAPI<Void>>> getUser(Api api) {
        return ResAPI.ok204();
    }
}
