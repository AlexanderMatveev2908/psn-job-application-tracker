package server.decorators.messages;

public enum ActT {
    OK("✅"),
    ERR("❌");

    private final String emj;

    ActT(String value) {
        this.emj = value;
    }

    public String getEmj() {
        return emj;
    }
}
