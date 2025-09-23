package server.decorators.flow;

import server.decorators.RootCls;

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

    public String getMsg() {
        return this.msg;
    }

    public int getStatus() {
        return this.status;
    }

    public Object getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }
}
