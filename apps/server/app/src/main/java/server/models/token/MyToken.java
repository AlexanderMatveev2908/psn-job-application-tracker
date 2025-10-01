package server.models.token;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.models.RootTable;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("tokens")
public class MyToken extends RootTable {

    @Column("user_id")
    private UUID userId;

    @Column("token_type")
    private TokenT tokenType;

    @Column("alg_type")
    private AlgT algType;

    @Column("hashed")
    private String hashed;

    @Column("exp")
    private Long exp;

    public MyToken(UUID id, UUID userId, AlgT algType, TokenT tokenType, String hashed, Long exp) {
        this.id = id;
        this.userId = userId;
        this.tokenType = tokenType;
        this.algType = algType;
        this.hashed = hashed;
        this.exp = exp;
    }

    public MyToken(UUID userId, AlgT algType, TokenT tokenType, String hashed, long exp) {
        this.userId = userId;
        this.tokenType = tokenType;
        this.algType = algType;
        this.hashed = hashed;
        this.exp = exp;
    }

    public MyToken() {
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

}
