package server.features.test.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.lib.data_structure.ShapeCheck;

@Component
public class PostTestCtrl {

    public Mono<ResponseEntity<ResAPI<Object>>> postMsg(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        })
                .flatMap(bd -> {
                    var msg = (String) bd.get("msg");

                    if (!ShapeCheck.isStr(msg))
                        return ResAPI.err400("missing msg");

                    return ResAPI.ok200("msg received", Map.of("clientMsg", msg));
                });
    }

    public Mono<ResponseEntity<ResAPI<Object>>> postFormData(Api api) {

        return ResAPI.ok200(
                "form data received • parsed • processed • sent back",
                api.getParsedForm().orElse(null));
    }

}
