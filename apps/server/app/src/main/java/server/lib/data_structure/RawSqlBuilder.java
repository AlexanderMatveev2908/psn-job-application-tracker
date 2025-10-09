package server.lib.data_structure;

import java.util.List;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import server.paperwork.pagination.PagSpec;

public class RawSqlBuilder {
  public static void appendILike(StringBuilder sql, List<Object> params, String key, String val) {
    String[] words = val.trim().split("\\s+");

    sql.append(" AND (");

    for (int i = 0; i < words.length; i++) {
      if (i > 0)
        sql.append(" AND ");

      String q = String.format("%s ILIKE $", key);

      sql.append(q).append(params.size() + 1);
      params.add("%" + words[i] + "%");
    }

    sql.append(")");
  }

  public static void isIn(StringBuilder sql, List<Object> params, String key, String cast, List<String> arg) {

    String q = String.format(" AND %s IN (", key);
    sql.append(q);

    for (int i = 0; i < arg.size(); i++) {
      if (i > 0)
        sql.append(", ");

      sql.append("$").append(params.size() + 1);

      if (cast != null)
        sql.append(String.format("::%s", cast));

      params.add(arg.get(i));
    }

    sql.append(")");
  }

  public static Statement createStmnt(Connection client, String sql, List<Object> params) {
    Statement stmt = client.createStatement(sql.toString());
    for (int i = 0; i < params.size(); i++)
      stmt.bind(i, params.get(i));

    return stmt;
  }

  public static Statement createHitsCounter(Connection client, StringBuilder sql, List<Object> params) {
    List<Object> cpyParams = List.copyOf(params);

    String txt = sql.toString();
    String replaced = txt.replaceFirst("(?i)select\\s+\\*", "SELECT COUNT(*)");

    Statement counter = RawSqlBuilder.createStmnt(client, replaced, cpyParams);

    return counter;

  }

  public static int withPagination(StringBuilder sql, List<Object> params, PagSpec form) {
    sql.append(" LIMIT $").append(params.size() + 1);
    params.add(form.getLimit());

    sql.append(" OFFSET $").append(params.size() + 1);
    int offset = form.getPage() * form.getLimit();
    params.add(offset);

    return offset;
  }

}
