package server.features.user.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.models.user.User;

@Component @RequiredArgsConstructor
public class GetUserCtrl {

    public Mono<ResponseEntity<ResAPI>> getUser(Api api) {

        User user = api.getUser();

        if (user == null)
            return new ResAPI(204).build();

        return new ResAPI(200).data(Map.of("user", user.forClient())).build();
    }
}
