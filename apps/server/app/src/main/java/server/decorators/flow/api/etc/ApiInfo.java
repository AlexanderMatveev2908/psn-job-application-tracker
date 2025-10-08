package server.decorators.flow.api.etc;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.ServerWebExchange;

import server.conf.Reg;
import server.lib.data_structure.ShapeCheck;

public interface ApiInfo {

  ServerWebExchange getExch();

  // ? hdr
  default String getHeader(String name) {
    return Optional.ofNullable(getExch().getRequest().getHeaders().getFirst(name)).orElse("");
  }

  default void addHeader(String k, Object v) {
    getExch().getResponse().getHeaders().add(k, String.valueOf(v));
  }

  // ? cookies
  default String getCookie(String name) {
    return Optional.ofNullable(getExch().getRequest().getCookies().getFirst(name)).map(cookie -> cookie.getValue())
        .orElse("");
  }

  // ? path var available at endpoint level
  default Optional<UUID> getPathVarId(String key) {
    Map<String, String> vars = getExch().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

    if (vars == null)
      return Optional.empty();

    String pathId = null;
    if (!Reg.isUUID((pathId = vars.get(key))))
      return Optional.empty();

    return Optional.of(UUID.fromString(pathId));
  }

  default Optional<UUID> getPathVarId() {
    String path = getPath();
    String[] parts = path.split("\\/");
    int lastIdx = parts.length - 1;
    String pathId;

    return ShapeCheck.isV4((pathId = parts[lastIdx])) ? Optional.of(UUID.fromString(pathId)) : Optional.empty();
  }

  default boolean hasPathUUID() {
    return getPathVarId().isPresent();
  }

  // ? query
  default String getQuery() {
    return Optional.ofNullable(getExch().getRequest().getURI().getQuery()).orElse("");
  }

  // ? tokens
  default String getJwt() {
    String auth = getHeader("authorization");
    String token = auth.startsWith("Bearer ") ? auth.substring("Bearer ".length()) : "";

    return token;
  }

  default String getJwe() {
    String jwe = getCookie("refreshToken");

    return jwe;
  }

  // ? general
  default String getPath() {
    return getExch().getRequest().getPath().toString().split("\\?", 2)[0];
  }

  default boolean isSamePath(String arg) {
    if (arg == null)
      return false;

    String endpoint = getPath();

    return endpoint.equals(arg);
  }

  default boolean isSamePath(String arg, HttpMethod method) {
    return isSamePath(arg) && getMethod().equals(method);
  }

  default boolean isSubPathOf(String arg) {
    if (arg == null)
      return false;

    return getPath().startsWith(arg);
  }

  default boolean isSubPathOf(String arg, HttpMethod method) {
    if (arg == null)
      return false;

    return getPath().startsWith(arg) && getMethod().equals(method);
  }

  default HttpMethod getMethod() {
    return getExch().getRequest().getMethod();
  }

  default String getContentType() {
    return Optional.ofNullable(getExch().getRequest().getHeaders().getContentType()).map(MediaType::toString)
        .orElse("");
  }

  default String getIp() {
    var req = getExch().getRequest();

    return Optional.ofNullable(req.getRemoteAddress()).map(addr -> addr.getAddress()).map(inet -> inet.getHostAddress())
        .orElse("unknown");
  }
}