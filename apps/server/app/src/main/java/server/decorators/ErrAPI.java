package server.decorators;

public class ErrAPI extends RuntimeException implements RootCls {

    public String msg;
    public int status;

    public ErrAPI(String msg, int status) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }
}
