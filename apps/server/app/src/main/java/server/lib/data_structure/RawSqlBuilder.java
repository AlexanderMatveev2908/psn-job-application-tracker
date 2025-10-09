package server.lib.data_structure;

import java.util.List;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;

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

  public static Statement create(Connection client, StringBuilder sql, List<Object> params) {
    Statement stmt = client.createStatement(sql.toString());
    for (int i = 0; i < params.size(); i++)
      stmt.bind(i, params.get(i));

    return stmt;
  }

}
