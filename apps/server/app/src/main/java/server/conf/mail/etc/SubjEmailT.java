package server.conf.mail.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubjEmailT {
    CONFIRM_EMAIL("✔️ CONFIRM EMAIL"),
    RECOVER_PWD("🔐 RECOVER PASSWORD");

    private final String value;
}
