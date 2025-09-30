package server.lib.security.expiry_mng.etc;

import java.util.Date;

public record RecExpJwt(Date now, Date exp) {

}
