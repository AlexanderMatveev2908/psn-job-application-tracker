package server.models.user;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.ShapeCheck;
import server.models.RootTable;

@Data @EqualsAndHashCode(callSuper = true) @Table("users")
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

    @Column("is_verified")
    private boolean isVerified;

    @Column("totp_secret")
    private String totpSecret;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return reflectiveToString();
    }

    public boolean use2FA() {
        return ShapeCheck.isStr(totpSecret);
    }

    public Map<String, Object> forClient() {
        Map<String, Object> data = new HashMap<>();

        Class<?> curr = this.getClass();

        while (curr != null && !curr.getName().startsWith("java.")) {
            for (Field field : curr.getDeclaredFields()) {
                int mods = field.getModifiers();

                if (Modifier.isStatic(mods) || Modifier.isTransient(mods))
                    continue;

                field.setAccessible(true);

                try {
                    String key = field.getName();

                    if (Stream.of("password", "totpSecret").anyMatch(sensitive -> sensitive.equals(key)))
                        continue;

                    Object val = field.get(this);
                    data.put(key, val);

                } catch (IllegalAccessException e) {
                    throw new ErrAPI("err copying user to map", 500);
                }
            }
            curr = curr.getSuperclass();
        }

        data.put("use2FA", getTotpSecret() != null);

        return data;
    }

    public static User fromTestPayload(Map<String, Object> arg) {
        try {
            if (arg == null)
                throw new ErrAPI("user null");

            String firstName = (String) arg.get("firstName");
            String lastName = (String) arg.get("lastName");
            String email = (String) arg.get("email");
            String password = (String) arg.get("password");

            return new User(firstName, lastName, email, password);
        } catch (Exception err) {
            throw new ErrAPI("invalid test data");
        }
    }

}
