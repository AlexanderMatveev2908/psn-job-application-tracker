package server.paperwork.pagination;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface PagSpec {
  @NotNull(message = "page required")
  @Min(value = 0, message = "page must be a positive integer or 0")
  Integer getPage();

  @NotNull(message = "limit required")
  @Min(value = 1, message = "limit must be a positive integer")
  Integer getLimit();
}