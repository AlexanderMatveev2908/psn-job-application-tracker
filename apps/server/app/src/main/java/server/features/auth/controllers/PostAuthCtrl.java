package server.features.auth.controllers;

import java.util.Map;

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
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenSvc;
import server.models.user.User;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostAuthCtrl {

    private final MyHashMng hashMng;
    private final RegisterSvc registerSvc;
    private final LoginSvc loginSvc;
    private final MyCookies myCookies;
    private final TokenSvc tokenSvc;

    public Mono<ResponseEntity<ResAPI>> register(Api api) {
        RegisterForm form = api.getMappedData();
        var us = new User(form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());

        return hashMng.argonHash(us.getPassword()).flatMap(hashed -> {
            us.setPassword(hashed);
            return registerSvc.register(us);
        }).flatMap(tpl -> {

            return new ResAPI(201).msg("user created").data(Map.of("accessToken", tpl.getT2())).cookie(tpl.getT1())
                    .build();
        });
    }

    public Mono<ResponseEntity<ResAPI>> login(Api api) {
        return loginSvc.login((LoginForm) api.getMappedData()).flatMap(tpl -> {
            return new ResAPI(200).msg("user logged").cookie(tpl.getT1()).data(Map.of("accessToken", tpl.getT2()))
                    .build();
        });
    }

    public Mono<ResponseEntity<ResAPI>> logout(Api api) {

        User us = api.getUser();
        ResAPI res = new ResAPI(200).msg("logged out").delCookie(myCookies.delJweCookie());

        if (us == null)
            return res.build();

        return tokenSvc.deleteByUserIdAndTokenT(us.getId(), TokenT.REFRESH).then(res.build());
    }
}
