package server.features.user.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.lib.security.mng_tokens.tokens.jwt.etc.MyJwtPayload;
import server.models.user.svc.UserSvc;

@Component @RequiredArgsConstructor
public class GetUserCtrl {

    private final UserSvc userSvc;

    public Mono<ResponseEntity<ResAPI>> getUser(Api api) {

        MyJwtPayload payload = api.getJwtPayload();

        if (payload == null)
            return new ResAPI(204).build();

        return userSvc.findById(payload.userId()).switchIfEmpty(Mono.error(new ErrAPI("user not found", 404)))
                .flatMap(user -> {

                    return new ResAPI(200).data(Map.of("user", user.forClient())).build();
                });
    }
}
