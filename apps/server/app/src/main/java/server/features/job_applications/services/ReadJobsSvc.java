package server.features.job_applications.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.conf.db.database.DB;
import server.decorators.flow.api.Api;
import server.features.job_applications.paperwork.read.QueryJobsForm;
import server.lib.data_structure.ShapeCheck;
import server.lib.data_structure.RawSqlBuilder;
import server.lib.data_structure.parser.Prs;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class ReadJobsSvc {
  private final DB db;

  public Mono<Map<String, Object>> mng(Api api) {

    QueryJobsForm form = api.getMappedData();
    UUID userId = api.getUser().getId();

    return db.trxLow(client -> {
      StringBuilder sql = new StringBuilder("""
              SELECT * FROM applications
              WHERE user_id = $1
          """);

      List<Object> params = new ArrayList<>();
      params.add(userId);

      if (ShapeCheck.isStr(form.getCompanyName()))
        RawSqlBuilder.appendILike(sql, params, "company_name", form.getCompanyName());
      if (ShapeCheck.isStr(form.getPositionName()))
        RawSqlBuilder.appendILike(sql, params, "position_name", form.getPositionName());

      if (ShapeCheck.isList(form.getStatus()))
        RawSqlBuilder.isIn(sql, params, "status", "application_status_t", form.getStatus());

      Statement stmt = RawSqlBuilder.create(client, sql, params);

      return Flux.from(stmt.execute())
          .flatMap(res -> res.map((row, meta) -> {
            var mapped = Prs.mapSql(row, meta);
            return mapped;
          }))
          .collectList().flatMap(applications -> {
            Map<String, Object> data = new HashMap<>();

            data.put("jobApplications", applications);
            data.put("nHits", applications.size());

            return Mono.just(data);
          });
    });

  }
}
