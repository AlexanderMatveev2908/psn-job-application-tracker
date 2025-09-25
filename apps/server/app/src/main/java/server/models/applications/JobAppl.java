package server.models.applications;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.models.RootTable;
import server.models.applications.etc.JobApplStatusT;

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
    private Long appliedAt;

    public JobAppl() {
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }
}
