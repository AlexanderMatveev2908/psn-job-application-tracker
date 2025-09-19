package server.features.wake_up.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import server.decorators.ErrAPI;
import server.decorators.ReqAPI;
import server.decorators.ResAPI;

@Component
public class PostCtrl {
    public ResponseEntity<ResAPI<Object>> wakeUp(ReqAPI req) throws ErrAPI, Exception {

        var bd = req.grabBody();
        var parsedQuery = req.getAttribute("parsedQuery");
        var parsedForm = req.getAttribute("parsedForm");

        System.out.println(bd);
        System.out.println(parsedQuery);
        System.out.println(parsedForm);

        // throw new ErrAPI("something", 500);

        return ResAPI.ok201(null);

        // return Map.of(
        // "status", 200,
        // "msg", "I am awake");
    }
}