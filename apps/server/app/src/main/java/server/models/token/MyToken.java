package server.models.token;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import server.decorators.RootCls;
import server.models.RootTable;
import server.models.token.side.AlgT;
import server.models.token.side.TokenT;

@Table("tokens")
public class MyToken extends RootTable implements RootCls {

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

    public MyToken(UUID userId, TokenT tokenType, AlgT algType, String hashed, Long exp) {
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public TokenT getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenT tokenType) {
        this.tokenType = tokenType;
    }

    public AlgT getAlgType() {
        return algType;
    }

    public void setAlgType(AlgT algType) {
        this.algType = algType;
    }

    public String getHashed() {
        return hashed;
    }

    public void setHashed(String hashed) {
        this.hashed = hashed;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

}
