package server.lib.security.mng_tokens.etc;

import java.util.Map;
import java.util.UUID;

import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;

public record MyTkPayload(UUID userId, long iat, long exp) {

  public static String toString(UUID usId, RecExpTplSec rec) {
    return Frmt.toJson(Map.of("userId", usId, "iat", rec.iat(), "exp", rec.exp()));
  }

  public static MyTkPayload fromString(String json) {
    try {
      Map<String, Object> obj = Frmt.jsonToMap(json);

      return fromObj(obj);
    } catch (Exception err) {
      throw new ErrAPI("jwt_invalid", 401);
    }
  }

  public static MyTkPayload fromObj(Map<String, Object> obj) {
    UUID usId = UUID.fromString((String) obj.get("userId"));
    long iat = Frmt.fromAnyToLong(obj.get("iat"));
    long exp = Frmt.fromAnyToLong(obj.get("exp"));

    return new MyTkPayload(usId, iat, exp);

  }
}