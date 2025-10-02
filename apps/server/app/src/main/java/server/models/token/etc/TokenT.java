package server.models.token.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.decorators.flow.ErrAPI;

@Getter @RequiredArgsConstructor
public enum TokenT {
    REFRESH("REFRESH"), CONF_EMAIL("CONF_EMAIL"), RECOVER_PWD("RECOVER_PWD"), RECOVER_PWD_2FA("RECOVER_PWD_2FA"),
    CHANGE_EMAIL("CHANGE_EMAIL"), CHANGE_EMAIL_2FA("CHANGE_EMAIL_2FA"), CHANGE_PWD("CHANGE_PWD"),
    MANAGE_ACC("MANAGE_ACC"), LOGIN_2FA("LOGIN_2FA"), MANAGE_ACC_2FA("MANAGE_ACC_2FA");

    private final String value;

    public static TokenT fromAny(Object arg) {

        try {
            if (arg instanceof String strArg)
                return TokenT.valueOf(strArg.toUpperCase());

            throw new ErrAPI("");
        } catch (Exception err) {
            throw new ErrAPI("invalid tokenT", 400);
        }

    }
}
