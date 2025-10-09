package server.features.job_applications.paperwork.read;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import server.models.applications.etc.JobApplStatusT;
import server.paperwork.enum_val.enum_list.EnumList;
import server.paperwork.enum_val.enum_match.EnumMatch;
import server.paperwork.pagination.PagSpec;
import server.paperwork.sorter.SorterSpec;
import server.paperwork.sorter.etc.SortValT;

@SuppressFBWarnings({ "EI2", "EI" })
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryJobsForm implements PagSpec, SorterSpec, QueryJobsSpec {
  private Integer page;
  private Integer limit;

  private String companyName;
  private String positionName;

  @EnumList(enumCls = JobApplStatusT.class)
  private List<String> status;

  @EnumMatch(enumCls = SortValT.class)
  private String createdAtSort;

  @EnumMatch(enumCls = SortValT.class)
  private String updatedAtSort;

  @EnumMatch(enumCls = SortValT.class)
  private String appliedAtSort;
}
