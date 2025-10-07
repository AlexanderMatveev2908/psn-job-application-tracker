package server.features.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;

@Component @RequiredArgsConstructor
public class GetTestCtrl {

    public Mono<ResponseEntity<ResAPI>> getLimited(Api api) {
        return new ResAPI(200).msg("get request limited 🚦").build();
    }

    public Mono<ResponseEntity<ResAPI>> getProtectedData(Api api) {
        return new ResAPI(200).msg("here you are protected data 🔐").build();
    }

    public Mono<ResponseEntity<ResAPI>> getTest(Api api) {
        return new ResAPI(200).msg("get request received 👻").build();
    }

}
