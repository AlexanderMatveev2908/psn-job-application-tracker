package server.features.job_applications.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

      // ? generic query
      if (ShapeCheck.isStr(form.getCompanyName()))
        RawSqlBuilder.appendILike(sql, params, "company_name", form.getCompanyName());
      if (ShapeCheck.isStr(form.getPositionName()))
        RawSqlBuilder.appendILike(sql, params, "position_name", form.getPositionName());
      if (ShapeCheck.isList(form.getStatus()))
        RawSqlBuilder.isIn(sql, params, "status", "application_status_t", form.getStatus());

      // ? create counter before inserting order or pagination not needed
      // ? to know n_hits 
      Statement hitsCounter = RawSqlBuilder.createHitsCounter(client, sql, params);

      // ? order logic
      List<String> orderParts = new ArrayList<>();

      if (ShapeCheck.isStr(form.getCreatedAtSort()))
        orderParts.add("created_at " + form.getCreatedAtSort());
      if (ShapeCheck.isStr(form.getUpdatedAtSort()))
        orderParts.add("updated_at " + form.getUpdatedAtSort());
      if (ShapeCheck.isStr(form.getAppliedAtSort()))
        orderParts.add("applied_at " + form.getAppliedAtSort());

      if (!orderParts.isEmpty())
        sql.append(" ORDER BY ").append(String.join(", ", orderParts));

      // ? manage pagination
      RawSqlBuilder.withPagination(sql, params, form);

      Statement stmt = RawSqlBuilder.createStmnt(client, sql.toString(), params);

      return Mono.from(hitsCounter.execute()).doOnError(err -> err.printStackTrace())
          .flatMapMany(resHits -> resHits.map((rowHits, metaHits) -> {
            Long nHits = rowHits.get(0, Long.class);

            return nHits;
          }))
          .single(0L)
          .flatMap(nHits -> Flux.from(stmt.execute())
              .flatMap(res -> res.map((row, meta) -> {
                var mapped = Prs.mapSql(row, meta);

                return mapped;
              }))
              .collectList()
              .map(applications -> {
                Map<String, Object> data = new LinkedHashMap<>();

                int pages = (int) Math.ceil((double) nHits / form.getLimit());
                data.put("nHits", nHits);
                data.put("pages", pages);
                data.put("hasPrevPage", form.getPage() > 0);
                data.put("hasNextPage", form.getPage() + 1 * form.getLimit() < nHits);
                data.put("jobApplications", applications);
                data.put("queryForm", form);

                return data;
              }));
    });
  }
}
