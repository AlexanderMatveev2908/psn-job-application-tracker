package server.lib.security.expiry_mng.etc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({ "EI" })
public record RecExpJwe(long now, long exp) {
}
