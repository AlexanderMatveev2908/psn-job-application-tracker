package server.models.applications;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.decorators.flow.api.Api;
import server.models.RootTable;
import server.models.applications.etc.JobApplStatusT;
import server.paperwork.job_application.JobApplForm;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("applications")
public class JobAppl extends RootTable {

    @Column("user_id")
    private UUID userId;

    @Column("company_name")
    private String companyName;

    @Column("position_name")
    private String positionName;

    @Column("status")
    private JobApplStatusT status;

    @Column("applied_at")
    private long appliedAt;

    @Column("notes")
    private String notes;

    public JobAppl() {
    }

    public JobAppl(UUID userId, String companyName, String positionName, JobApplStatusT status, long appliedAt,
            String notes) {
        this.userId = userId;
        this.companyName = companyName;
        this.positionName = positionName;
        this.status = status;
        this.appliedAt = appliedAt;
        this.notes = notes;
    }

    public JobAppl(UUID id, UUID userId, String companyName, String positionName, JobApplStatusT status, long appliedAt,
            String notes) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.positionName = positionName;
        this.status = status;
        this.appliedAt = appliedAt;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

    public static JobAppl fromUserForm(UUID userId, JobApplForm form) {
        return new JobAppl(userId, form.getCompanyName(), form.getPositionName(), form.getStatusT(),
                form.getAppliedAtAsLong(), form.getNotes());
    }

    public static JobAppl fromAttrApi(Api api) {

        JobApplForm form = api.getMappedData();
        UUID userId = api.getUser().getId();
        UUID jobApplId = api.getPathVarId().get();

        return new JobAppl(jobApplId, userId, form.getCompanyName(), form.getPositionName(), form.getStatusT(),
                form.getAppliedAtAsLong(), form.getNotes());
    }

}
