package server.decorators;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import server.decorators.flow.ErrAPI;

public interface RootCls {
    default String reflectiveToString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName() + "{");

        try {
            Class<?> curr = this.getClass();

            while (curr != null && !curr.getName().startsWith("java.")) {
                for (Field f : curr.getDeclaredFields()) {
                    int mods = f.getModifiers();
                    if (Modifier.isStatic(mods))
                        continue;
                    if (Modifier.isTransient(mods))
                        continue;

                    f.setAccessible(true);
                    sb.append(f.getName()).append("=").append(f.get(this)).append(", ");
                }
                curr = curr.getSuperclass();
            }

        } catch (IllegalAccessException e) {
            throw new ErrAPI("err cstm toString method");
        }

        if (sb.lastIndexOf(", ") == sb.length() - 2)
            sb.setLength(sb.length() - 2);

        sb.append("}");

        return sb.toString();
    }

}
