package server.decorators.flow;

import java.util.Map;

import lombok.Getter;
import server.decorators.RootCls;

@Getter
public final class ErrAPI extends RuntimeException implements RootCls {

    private final String msg;
    private final int status;
    private final Map<String, Object> data;

    public ErrAPI(String msg, int status, Map<String, Object> data) {
        super("❌ " + msg);
        this.msg = super.getMessage();
        this.status = status;
        this.data = data;
    }

    public ErrAPI(String msg, int status) {
        this(msg, status, null);
    }

    public ErrAPI(String msg) {
        this(msg, 500, null);
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

}
