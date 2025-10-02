package server.features.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.test.services.GetUserTestSvc;

@Component @RequiredArgsConstructor
public class GetTestCtrl {
    private final GetUserTestSvc testUserSvc;

    public Mono<ResponseEntity<ResAPI>> getLimited(Api api) {
        return new ResAPI(200).msg("get request limited 🚦").build();
    }

    public Mono<ResponseEntity<ResAPI>> getTest(Api api) {
        return new ResAPI(200).msg("get request received 👻").build();
    }

    public Mono<ResponseEntity<ResAPI>> getUserTest(Api api) {
        return testUserSvc.getUserTest(api)
                .flatMap(data -> new ResAPI(200).msg("data test generated as").data(data).build());
    }
}
