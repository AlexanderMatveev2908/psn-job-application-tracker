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
import server.features.auth.paperwork.LoginForm;
import server.features.auth.paperwork.RegisterForm;
import server.features.auth.services.LoginSvc;
import server.features.auth.services.RegisterSvc;
import server.lib.security.cookies.MyCookies;
import server.lib.security.hash.MyHashMng;
import server.models.user.User;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostAuthCtrl {

    private final MyHashMng hashMng;
    private final RegisterSvc registerSvc;
    private final LoginSvc loginSvc;
    private final MyCookies myCookies;

    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        RegisterForm form = api.getMappedData();
        var us = new User(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());

        return hashMng.argonHash(us.getPassword()).flatMap(hashed -> {
            us.setPassword(hashed);
            return registerSvc.register(us);
        }).flatMap(tpl -> {

            ResponseCookie refreshCookie = myCookies.jweCookie(tpl.getT1());

            return new ResAPI(201).msg("user created").data(Map.of("accessToken", tpl.getT2())).cookie(refreshCookie)
                    .build();
        });
    }

    public Mono<ResponseEntity<ResAPI>> login(Api api) {
        return loginSvc.login((LoginForm) api.getMappedData()).flatMap(tpl -> {

            ResponseCookie refreshCookie = myCookies.jweCookie(tpl.getT1());

            return new ResAPI(200).msg("user logged").cookie(refreshCookie).data(Map.of("accessToken", tpl.getT2()))
                    .build();
        });
    }
}
