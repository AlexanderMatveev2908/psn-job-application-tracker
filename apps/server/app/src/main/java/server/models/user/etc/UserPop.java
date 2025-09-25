package server.models.user.etc;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.token.MyToken;
import server.models.user.User;

@SuppressFBWarnings({ "EI" })
public record UserPop(User user, List<MyToken> tokens) {
}
