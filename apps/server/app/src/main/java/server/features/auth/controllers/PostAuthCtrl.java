package server.features.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.auth.paperwork.RegisterForm;
import server.lib.jwt.MyJwt;
import server.models.user.User;
import server.models.user.svc.UserSvc;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class PostAuthCtrl {

    private final UserSvc userSvc;
    private final MyJwt myJwt;

    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        RegisterForm form = api.getMappedData();
        var us = new User(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());

        return us.hashPwd().flatMap(hash -> {
            return userSvc.insert(us);
        }).flatMap(saved -> {

            String accessToken = myJwt.create(saved.getId());

            return ResAPI.ok201("user created", Map.of("user", saved, "accessToken", accessToken));
        });
    }
}
