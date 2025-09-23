package server.decorators;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import server.decorators.flow.ErrAPI;

public interface RootCls {
    default String reflectiveToString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName() + "{");

        try {
            Class<?> current = this.getClass();

            while (current != null && !current.getName().startsWith("java.")) {
                for (Field f : current.getDeclaredFields()) {
                    int mods = f.getModifiers();
                    if (Modifier.isStatic(mods))
                        continue;
                    if (Modifier.isTransient(mods))
                        continue;

                    f.setAccessible(true);
                    sb.append(f.getName())
                            .append("=")
                            .append(f.get(this))
                            .append(", ");
                }
                current = current.getSuperclass();
            }

        } catch (IllegalAccessException e) {
            throw new ErrAPI("err in cst toString app method", 500);
        }

        if (sb.lastIndexOf(", ") == sb.length() - 2)
            sb.setLength(sb.length() - 2);

        sb.append("}");

        return sb.toString();
    }

}
