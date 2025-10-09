package server.paperwork.sorter.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortValT {
  ASC("ASC"),
  DESC("DESC");

  private final String value;
}
