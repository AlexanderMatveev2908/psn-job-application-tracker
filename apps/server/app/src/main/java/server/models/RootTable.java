package server.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.decorators.RootCls;

@Data
@EqualsAndHashCode()
public class RootTable implements RootCls {
    @Id
    private UUID id;

    @Column("created_at")
    private Long createdAt;

    @Column("updated_at")
    private Long updatedAt;

    @Column("deleted_at")
    private Long deletedAt;

    @Override
    public String toString() {
        return reflectiveToString();
    }

}
