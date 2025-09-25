package server.models.user.etc;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.applications.JobAppl;
import server.models.backup_code.BkpCodes;
import server.models.token.MyToken;
import server.models.user.User;

@SuppressFBWarnings({ "EI" })
public record UserPop(User user, List<MyToken> tokens, List<BkpCodes> backupCodes, List<JobAppl> applications) {
}
