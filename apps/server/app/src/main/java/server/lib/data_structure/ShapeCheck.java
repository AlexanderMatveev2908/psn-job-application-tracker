package server.lib.data_structure;

import java.util.UUID;

import server.conf.Reg;

public final class ShapeCheck {
    public static boolean isStr(Object val) {
        if (val instanceof String str)
            return !str.isBlank();

        return false;
    }

    public static boolean isV4(String arg) {
        try {
            var res = Reg.isUUID(arg);
            UUID.fromString(arg);

            return res;
        } catch (Exception err) {
            return false;
        }
    }
}
