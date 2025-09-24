package server.models.user.side;

import java.util.UUID;

public record UserRecord(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String tmpEmail,
        String password,
        String totpSecret,
        String tokens) {
}
