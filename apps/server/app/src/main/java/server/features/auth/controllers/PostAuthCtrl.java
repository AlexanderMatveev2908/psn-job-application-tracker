package server.features.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.auth.paperwork.RegisterForm;
import server.lib.security.cookies.MyCookies;
import server.models.user.User;
import server.models.user.svc.UserSvc;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class PostAuthCtrl {

    private final UserSvc userSvc;
    private final MyCookies myCookies;

    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        RegisterForm form = api.getMappedData();
        var us = new User(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());

        return us.hashPwd().flatMap(hashedUser -> {
            return userSvc.insert(hashedUser);
        }).flatMap(tpl -> {

            ResponseCookie refreshCookie = myCookies.genRefreshCookie(tpl.getT2().getHashed());

            return new ResAPI(201).msg("user created")
                    .data(Map.of("user", tpl.getT1(), "accessToken", tpl.getT3())).cookie(refreshCookie).build();
        });
    }
}
