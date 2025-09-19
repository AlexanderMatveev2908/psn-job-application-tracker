package server.decorators;

public enum MapperMsg {
    OK_200(200, "ok"),
    OK_201(201, "ok 201"),

    ERR_400(400, "bad request"),
    ERR_401(401, "unauthorized"),
    ERR_403(403, "forbidden"),
    ERR_404(404, "not found"),
    ERR_409(409, "conflict"),
    ERR_422(422, "unprocessable entity"),
    ERR_500(500, "A wild slime appeared! Server takes damage of 500 hp ⚔️");

    private final int code;
    private final String msg;

    MapperMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static MapperMsg fromCode(int code) {
        for (MapperMsg m : values())
            if (m.code == code)
                return m;

        return null;
    }
}
