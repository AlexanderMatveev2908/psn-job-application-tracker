package server.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import server.decorators.flow.ReqAPI;
import server.decorators.flow.ResAPI;

@Component
public class GetUserCtrl {

    public ResponseEntity<Void> getUser(ReqAPI req) {
        return ResAPI.ok204();
    }
}
