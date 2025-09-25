package server.models.user.side;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.token.MyToken;
import server.models.user.User;

@SuppressFBWarnings({ "EI" })
public record UserPop(User user, List<MyToken> tokens) {
}
