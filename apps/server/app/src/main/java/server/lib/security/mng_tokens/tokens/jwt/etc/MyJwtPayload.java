package server.lib.security.mng_tokens.tokens.jwt.etc;

import java.util.Map;
import java.util.UUID;

import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;

public record MyJwtPayload(UUID userId) {

  public static String toString(UUID usId) {
    return Frmt.toJson(Map.of("userId", usId));
  }

  public static MyJwtPayload fromString(String json) {

    try {
      return new MyJwtPayload(UUID.fromString((String) Frmt.jsonToMap(json).get("userId")));

    } catch (Exception err) {
      throw new ErrAPI("jwt_invalid", 401);
    }
  }
}