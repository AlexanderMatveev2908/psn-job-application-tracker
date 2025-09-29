package server.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@Component
public class GetUserCtrl {

    public Mono<ResponseEntity<ResAPI>> getUser(Api api) {
        return new ResAPI(204).build();
    }
}
