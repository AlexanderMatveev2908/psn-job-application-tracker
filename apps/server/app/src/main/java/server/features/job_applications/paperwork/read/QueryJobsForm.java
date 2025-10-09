package server.features.job_applications.paperwork.read;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import server.models.applications.etc.JobApplStatusT;
import server.paperwork.enum_val.enum_list.EnumList;
import server.paperwork.pagination.PagSpec;

@SuppressFBWarnings({ "EI2", "EI" })
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryJobsForm implements PagSpec, QueryJobsSpec {
  private Integer page;
  private Integer limit;

  private String companyName;
  private String positionName;

  @EnumList(enumCls = JobApplStatusT.class)
  private List<String> status;
}
