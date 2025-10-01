package server.models.user;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;
import server.lib.security.hash.MyArgonHash;
import server.models.RootTable;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("users")
public class User extends RootTable {

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("tmp_email")
    private String tmpEmail;

    @Column("password")
    private String password;

    @Column("totp_secret")
    private String totpSecret;

    public User() {
    }

    public User(String firstName, String lastName, String email,
            String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

    public Mono<User> hashPwd() {
        return MyArgonHash.rctHash(password)
                .map(hash -> {
                    setPassword(hash);
                    return this;
                });
    }

}
