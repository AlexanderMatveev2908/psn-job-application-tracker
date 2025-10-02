package server._lib_tests.shapes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum ExpArgT {
  JWT("jwt"), JWE("jwe"), CBC_HMAC("cbc_hmac");

  private final String value;
}
