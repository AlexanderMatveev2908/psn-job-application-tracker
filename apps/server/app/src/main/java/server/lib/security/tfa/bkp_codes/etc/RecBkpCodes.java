package server.lib.security.tfa.bkp_codes.etc;

import java.util.List;

public record RecBkpCodes(List<String> clientCodes, List<String> hashed) {
}