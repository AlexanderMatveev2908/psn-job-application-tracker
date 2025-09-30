package server.lib.security.expiry_mng.etc;

import java.util.Date;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({ "EI" })
public record RecExpJwt(Date now, Date exp) {
}
