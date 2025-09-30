package server.lib.dev;

import server.decorators.flow.ErrAPI;

public class ThErr {

    public static void main(String[] args) {
        throw new ErrAPI("I thrown a random error 😈");

    }
}
