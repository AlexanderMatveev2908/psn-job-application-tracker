package server.lib.dev;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import server.decorators.ErrAPI;

public class MyLog {

    private static final String APP_PKG = "server";

    public static void logTtl(String title, Object... arg) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        StackTraceElement caller = Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(f -> f.getClassName().startsWith(APP_PKG))
                .filter(f -> !f.getClassName().contains(MyLog.class.getSimpleName()))
                .findFirst()
                .orElse(null);

        String fileName = (caller != null) ? caller.getFileName() : "unknown caller";
        String thread = Thread.currentThread().getName();

        if (title != null)
            System.out.printf("⏰ %s • 🗃️ %s • 🧵 %s%n%s%n", time, fileName, thread, title);
        else
            System.out.printf("⏰ %s • 🗃️ %s • 🧵 %s%n", time, fileName, thread);

        if (arg == null)
            return;
        for (Object v : arg)
            System.out.println(v);
    }

    public static void log(Object... arg) {
        logTtl(null, arg);
    }

    public static void logErr(Exception err) {

        if (err == null) {
            logTtl("⚠️ null error passed to logErr ⚠️");
            return;
        }

        logTtl(err instanceof ErrAPI ? "💣 " + err.toString() : "❌ unexpected err");

        StackTraceElement[] frames = err.getStackTrace();

        System.out.println("\t");

        for (StackTraceElement f : frames) {
            if (f.toString().startsWith("server")) {
                System.out.printf("📂 %s => 🔢 %d | 🆎 %s | ☢️ %s%n",
                        f.getFileName(),
                        f.getLineNumber(),
                        f.getMethodName(),
                        f.toString());
            }
        }

        String msg = err.getMessage();
        int depth = frames.length;
        StackTraceElement last = depth > 0 ? frames[0] : null;

        System.out.println("\t");
        System.out.printf("📝 msg => %s%n", msg);
        System.out.printf("📏 depth => %d%n", depth);

        if (last == null)
            return;

        System.out.printf("💥 last file => 📁 %s%n", last.getFileName());
        System.out.printf("📏 last line => %d%n", last.getLineNumber());
        System.out.printf("👻 last cb name => %s%n", last.getMethodName());

    }
}
