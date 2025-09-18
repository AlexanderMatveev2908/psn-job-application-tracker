package server.lib.dev;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class MyLog {

    private static final String APP_PKG = "server";

    public void logT(String title, Object... arg) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        StackTraceElement caller = Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(f -> f.getClassName().startsWith(APP_PKG))
                .filter(f -> !f.getClassName().contains(MyLog.class.getSimpleName()))
                .findFirst()
                .orElse(null);

        String fileName = (caller != null) ? caller.getFileName() : "unknown caller";
        String thread = Thread.currentThread().getName();

        if (title != null)
            System.out.printf("⏰ %s • 🗃️ %s • 🧵 %s • 📌 %s%n", time, fileName, thread, title);
        else
            System.out.printf("⏰ %s • 🗃️ %s • 🧵 %s%n", time, fileName, thread);

        if (arg == null)
            return;
        for (Object v : arg)
            System.out.println(v);
    }

    public void log(Object... arg) {
        this.logT(null, arg);
    }
}
