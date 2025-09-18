package server.features.wake_up.controllers;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GetCtrl {
    public Map<String, Object> wakeUp() {
        return Map.of(
                "status", 200,
                "msg", "I am awake");
    }
}