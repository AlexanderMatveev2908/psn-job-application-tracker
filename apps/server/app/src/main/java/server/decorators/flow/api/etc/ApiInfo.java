package server.decorators.flow.api.etc;

import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;

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
    return getExch().getRequest().getPath().toString();
  }

  default boolean isSamePath(String arg) {
    if (arg == null)
      return false;

    return getPath().equals(arg.split("\\?", 2)[0]);
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