package server.models.token.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.decorators.flow.ErrAPI;

@Getter @RequiredArgsConstructor
public enum TokenT {
    REFRESH("REFRESH", "N/A"), CONF_EMAIL("CONF_EMAIL", "✔️ CONFIRM EMAIL"),
    RECOVER_PWD("RECOVER_PWD", "🔐 RECOVER PASSWORD"), RECOVER_PWD_2FA("RECOVER_PWD_2FA", "🔐 RECOVER PASSWORD (2FA)"),
    CHANGE_EMAIL("CHANGE_EMAIL", "📮 CHANGE EMAIL"), CHANGE_EMAIL_2FA("CHANGE_EMAIL_2FA", "✉️ CHANGE EMAIL (2FA)"),
    CHANGE_PWD("CHANGE_PWD", "🔑 CHANGE PASSWORD"), MANAGE_ACC("MANAGE_ACC", "N/A"), LOGIN_2FA("LOGIN_2FA", "N/A"),
    MANAGE_ACC_2FA("MANAGE_ACC_2FA", "N/A");

    private final String value;
    private final String subject;

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
