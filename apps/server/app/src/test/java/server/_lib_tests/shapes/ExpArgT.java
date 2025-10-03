package server._lib_tests.shapes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter @RequiredArgsConstructor
public enum ExpArgT {
  JWT("jwt"), JWE("jwe"), CBC_HMAC("cbc_hmac");

  private final String value;

  public static ExpArgT fromSplit(String arg) {
    String[] val = arg.split("_expired", 2);

    if (val.length < 2)
      return null;

    return Arrays.stream(ExpArgT.values()).filter(t -> t.name().equalsIgnoreCase(val[0])).findFirst().orElse(null);
  }
}
