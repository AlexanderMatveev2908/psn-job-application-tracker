package server.paperwork.job_application.read;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import server.conf.Reg;

public interface QueryJobsSpec {
  @Pattern(regexp = Reg.JOB_NAME, message = "company name invalid")
  @Size(max = 100, message = "max length company name exceeded")
  String getCompanyName();

  @Pattern(regexp = Reg.JOB_NAME, message = "position name invalid")
  @Size(max = 100, message = "max length position name exceeded")
  String getPositionName();

  List<String> getStatus();
}
