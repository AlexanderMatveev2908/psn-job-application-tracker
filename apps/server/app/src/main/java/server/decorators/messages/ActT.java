package server.decorators.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActT {
    OK("✅"),
    ERR("❌");

    private final String emj;

}
