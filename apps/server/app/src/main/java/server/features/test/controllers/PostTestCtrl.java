package server.features.test.controllers;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.features.test.services.PostFormSvc;
import server.features.test.services.get_user_test.GetUserTestSvc;
import server.lib.data_structure.ShapeCheck;
import server.lib.security.cookies.MyCookies;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostTestCtrl {
    private final PostFormSvc postFormSvc;
    private final GetUserTestSvc testUserSvc;
    private final MyCookies ckMng;

    public Mono<ResponseEntity<ResAPI>> postMsg(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        }).flatMap(bd -> {
            var msg = (String) bd.get("msg");

            if (!ShapeCheck.isStr(msg))
                return new ResAPI(400).msg("missing msg").build();

            return new ResAPI(200).msg("msg received").data(Map.of("clientMsg", msg)).build();
        }).switchIfEmpty(Mono.error(new ErrAPI("missing msg", 400)));
    }

    public Mono<ResponseEntity<ResAPI>> getUserTest(Api api) {
        return testUserSvc.getUserTest(api).flatMap(data -> {

            ResponseCookie jweCk = ckMng.jweCookie((String) data.get("refreshToken"));

            return new ResAPI(200).msg("data test generated as requested").cookie(jweCk).data(data).build();
        });
    }

    public Mono<ResponseEntity<ResAPI>> postFormData(Api api) {
        return postFormSvc.postForm(api).flatMap(tpl -> {
            return new ResAPI(200).msg(
                    "form parsed • processed • saved locally • uploaded on cloud • deleted locally • deleted from cloud")
                    .data(Map.of("saved", tpl.getT1(), "deleted", tpl.getT2())).build();
        });

    }

}
