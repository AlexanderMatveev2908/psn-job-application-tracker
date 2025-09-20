package server.features.test.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import server.decorators.ErrAPI;
import server.decorators.flow.ReqAPI;
import server.decorators.flow.ResAPI;
import server.lib.data_structure.ShapeCheck;

@Component
public class PostTestCtrl {
    public ResponseEntity<ResAPI<Map<String, String>>> postMsg(ReqAPI req) {

        @SuppressWarnings("unchecked")
        var bd = (Map<String, Object>) req.grabBody();

        String msg;
        if (bd == null || !ShapeCheck.isStr(msg = (String) bd.get("msg")))
            throw new ErrAPI("missing msg", 400);

        return ResAPI.ok200("msg received", Map.of("clientMsg", msg));

    }

    public ResponseEntity<ResAPI<Object>> postFormData(ReqAPI req) {
        return ResAPI.ok200("form data received • parsed • processed • sent back", req.getAttribute("parsedForm"));
    }
}
