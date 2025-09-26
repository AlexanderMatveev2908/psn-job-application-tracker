package server.decorators.flow;

import lombok.Getter;
import server.decorators.RootCls;

@Getter
public class ErrAPI extends RuntimeException implements RootCls {

    private final String msg;
    private final int status;
    private final Object data;

    public ErrAPI(String msg, int status, Object data) {
        super(msg);
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    public ErrAPI(String msg, int status) {
        this(msg, status, null);
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

    @Override
    public String getMessage() {
        return getMsg();
    }
}
