package server.features.wake_up.controllers;

import java.util.Map;

import org.springframework.stereotype.Component;

import server.decorators.ErrAPI;
import server.decorators.ReqAPI;

@Component
public class PostCtrl {
    public Map<String, Object> wakeUp(ReqAPI req) throws ErrAPI, Exception {

        // var bd = req.grabBody();

        // System.out.println(bd);

        throw new ErrAPI("something", 500);

        // return Map.of(
        // "status", 200,
        // "msg", "I am awake");
    }
}