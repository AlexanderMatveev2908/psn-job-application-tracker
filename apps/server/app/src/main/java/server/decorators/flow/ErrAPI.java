package server.decorators.flow;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.decorators.RootCls;

@Getter
@RequiredArgsConstructor
public class ErrAPI extends RuntimeException implements RootCls {

    private final String msg;
    private final int status;
    private final Object data;

    public ErrAPI(String msg, int status) {
        this(msg, status, null);
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }
}
