package server.models.backup_code.etc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.models.backup_code.BkpCodes;

@SuppressFBWarnings({ "EI", "EI2" })
public record RecInfoBkp(BkpCodes bkpMatch, int codesCount) {
}