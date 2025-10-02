package server.lib.data_structure;

public final class ShapeCheck {
    public static boolean isStr(Object val) {
        if (val instanceof String str)
            return !str.isBlank();

        return false;
    }
}
