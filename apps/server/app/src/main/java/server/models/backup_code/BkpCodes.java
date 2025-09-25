package server.models.backup_code;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.models.RootTable;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("backup_codes")
public class BkpCodes extends RootTable {
    @Column("user_id")
    private UUID userId;

    @Column("code")
    private String code;

    public BkpCodes() {
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

}
