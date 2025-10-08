package server.paperwork.job_application;

import java.time.LocalDate;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;
import server.models.applications.etc.JobApplStatusT;
import server.paperwork.enum_val.EnumMatch;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class JobApplForm {
  @NotBlank(message = "company name required") @Pattern(regexp = Reg.JOB_NAME, message = "company name invalid") @Size(max = 100, message = "max length company name exceeded")
  private String companyName;

  @NotBlank(message = "position name required") @Pattern(regexp = Reg.JOB_NAME, message = "position name invalid") @Size(max = 100, message = "max length position name exceeded")
  private String positionName;

  @NotBlank(message = "status required") @EnumMatch(enumCls = JobApplStatusT.class)
  private String status;

  @Pattern(regexp = Reg.TXT, message = "notes invalid") @Size(max = 1000, message = "max length notes exceeded")
  private String notes;

  @Pattern(regexp = Reg.DATE_PICKER, message = "date invalid")
  private String appliedAt;

  public JobApplStatusT getStatusT() {
    return JobApplStatusT.valueOf(status);
  }

  public long getAppliedAtAsLong() {
    try {
      LocalDate date = LocalDate.parse(appliedAt);
      return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    } catch (Exception err) {
      throw new ErrAPI("invalid applied_at date");
    }
  }

}
