package server.features.wake_up.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import server.decorators.ErrAPI;
import server.decorators.flow.ReqAPI;
import server.decorators.flow.ResAPI;

@Component
public class PostCtrl {
    public ResponseEntity<ResAPI<Object>> wakeUp(ReqAPI req) throws ErrAPI, Exception {

        var parsedForm = req.getAttribute("parsedForm");

        System.out.println(parsedForm);

        // throw new ErrAPI("something", 500);

        return ResAPI.ok201(parsedForm);

        // return Map.of(
        // "status", 200,
        // "msg", "I am awake");
    }
}