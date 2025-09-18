package server.features.wake_up.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class PostCtrl {
    public Map<String, Object> wakeUp(HttpServletRequest req) {
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String query = req.getQueryString();
        String auth = req.getHeader("authorization");

        String body;
        try (BufferedReader reader = req.getReader()) {
            body = reader.lines().collect(Collectors.joining("\n"));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> data = mapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });

        } catch (IOException e) {
            body = "❌ error reading body: " + e.getMessage();
        }

        return Map.of(
                "status", 200,
                "msg", "I am awake");
    }
}