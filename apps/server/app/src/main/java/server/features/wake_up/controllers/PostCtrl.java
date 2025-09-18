package server.features.wake_up.controllers;

import java.util.Map;

import org.springframework.stereotype.Component;

import server.middleware.dev.ReqAPI;

@Component
public class PostCtrl {
    public Map<String, Object> wakeUp(ReqAPI req) {

        // var bd = req.grabBody();

        // System.out.println(bd);

        return Map.of(
                "status", 200,
                "msg", "I am awake");
    }
}