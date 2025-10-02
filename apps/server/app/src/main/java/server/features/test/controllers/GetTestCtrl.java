package server.features.test.controllers;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.test.services.GetUserTestSvc;
import server.lib.security.cookies.MyCookies;

@Component @RequiredArgsConstructor
public class GetTestCtrl {
    private final GetUserTestSvc testUserSvc;
    private final MyCookies ckMng;

    public Mono<ResponseEntity<ResAPI>> getLimited(Api api) {
        return new ResAPI(200).msg("get request limited 🚦").build();
    }

    public Mono<ResponseEntity<ResAPI>> getProtectedData(Api api) {
        return new ResAPI(200).msg("here you are protected data 🔐").build();
    }

    public Mono<ResponseEntity<ResAPI>> getTest(Api api) {
        return new ResAPI(200).msg("get request received 👻").build();
    }

    public Mono<ResponseEntity<ResAPI>> getUserTest(Api api) {
        return testUserSvc.getUserTest(api).flatMap(data -> {

            ResponseCookie jweCk = ckMng.jweCookie((String) data.get("refreshToken"));
            data.remove("refreshToken");

            return new ResAPI(200).msg("data test generated as requested").cookie(jweCk).data(data).build();
        });
    }
}
