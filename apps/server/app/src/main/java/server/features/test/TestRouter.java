package server.features.test;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import server.decorators.flow.ReqAPI;
import server.decorators.flow.ResAPI;
import server.features.test.controllers.PostTestCtrl;
import server.router.RootApi;

@RootApi
public class TestRouter {

    private final PostTestCtrl postCtrl;

    public TestRouter(PostTestCtrl postCtrl) {
        this.postCtrl = postCtrl;
    }

    @PostMapping("/test")
    public ResponseEntity<ResAPI<Map<String, String>>> postMsg(ReqAPI req) {
        return postCtrl.postMsg(req);
    }

    @PostMapping("/test-form-data")
    public ResponseEntity<ResAPI<Object>> postFormData(ReqAPI req) {
        return postCtrl.postFormData(req);
    }

}
