package server.decorators.flow.api.etc;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.server.ServerWebExchange;

import server.models.applications.JobAppl;
import server.models.backup_code.etc.RecInfoBkp;
import server.models.user.User;

public interface ApiAttr {
  ServerWebExchange getExch();

  default <T> void setAttr(String key, T value) {
    if (key == null)
      return;
    if (value == null)
      getExch().getAttributes().remove(key);
    else
      getExch().getAttributes().put(key, value);

  }

  // ? user
  default void setUserAttr(User user) {
    setAttr("user", user);
  }

  default User getUser() {
    return getExch().getAttribute("user");
  }

  // ? curr job application
  default void setCurrApplication(JobAppl job) {
    setAttr("jobApplication", job);
  }

  default JobAppl getCurrApplication() {
    return getExch().getAttribute("jobApplication");
  }

  // ? bkp codes
  default void setInfoBkp(RecInfoBkp rec) {
    setAttr("recInfoBkp", rec);
  }

  default Optional<RecInfoBkp> getInfoBkp() {
    RecInfoBkp rec = getExch().getAttribute("recInfoBkp");

    return Optional.ofNullable(rec);
  }

  default void putCodesLeftIfPresent(Map<String, Object> data) {
    Optional<RecInfoBkp> rec = getInfoBkp();

    if (rec.isPresent())
      data.put("codesLeft", rec.get().codesCount() - 1);
  }

  // ? instance form parsed in mdw and set before svc or ctrl
  default <T> void setMappedDataAttr(T data) {
    setAttr("mappedData", data);
  }

  default <T> T getMappedData() {
    return getExch().getAttribute("mappedData");
  }

  // ? parsed query
  default void setParsedQueryAttr(Map<String, Object> parsed) {
    setAttr("parsedQuery", parsed);
  }

  @SuppressWarnings({ "unused", })
  default Optional<Map<String, Object>> getParsedQuery() {
    Map<String, Object> val = getExch().getAttribute("parsedQuery");

    return val != null ? Optional.of(val) : Optional.empty();
  }

  default String getQueryCbcHmac() {
    if (getParsedQuery().orElse(Map.of()).get("cbcHmacToken") instanceof String cbcHmac)
      return cbcHmac;

    return "";
  }

  // ? parsed form
  default void setParsedFormAttr(Map<String, Object> parsed) {
    setAttr("parsedForm", parsed);
  }

  default Optional<Map<String, Object>> getParsedForm() {
    Map<String, Object> val = getExch().getAttribute("parsedForm");

    return val != null ? Optional.of(val) : Optional.empty();

  }

}
