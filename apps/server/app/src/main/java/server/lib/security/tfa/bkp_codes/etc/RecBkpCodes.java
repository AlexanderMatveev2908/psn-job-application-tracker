package server.lib.security.tfa.bkp_codes.etc;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({ "EI2", "EI" })
public record RecBkpCodes(List<String> clientCodes, List<String> hashed) {
}