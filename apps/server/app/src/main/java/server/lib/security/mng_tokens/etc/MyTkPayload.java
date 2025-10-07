package server.lib.security.mng_tokens.etc;

import java.util.Map;
import java.util.UUID;

import server.decorators.flow.ErrAPI;
import server.lib.data_structure.parser.Prs;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;

public record MyTkPayload(UUID userId, long iat, long exp) {

  public static String toString(UUID usId, RecExpTplSec rec) {
    return Prs.toJson(Map.of("userId", usId, "iat", rec.iat(), "exp", rec.exp()));
  }

  public static MyTkPayload fromString(String json) {
    try {
      Map<String, Object> obj = Prs.jsonToMap(json);

      return fromMap(obj);
    } catch (Exception err) {
      throw new ErrAPI("jwt_invalid", 401);
    }
  }

  public static MyTkPayload fromMap(Map<String, Object> obj) {
    UUID usId = UUID.fromString((String) obj.get("userId"));
    long iat = Prs.fromAnyToLong(obj.get("iat"));
    long exp = Prs.fromAnyToLong(obj.get("exp"));

    return new MyTkPayload(usId, iat, exp);

  }
}