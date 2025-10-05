package server.lib.dev;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.lib.data_structure.Prs;
import server.lib.paths.Hiker;

public final class MyLog {

    private static final String APP_PKG = "server";
    private static final ExecutorService logThread = Executors.newSingleThreadExecutor();

    public static void limiter() {
        System.out.println("-".repeat(60));
    }

    public static void startLog() {
        System.out.println("\n");
        limiter();
    }

    public static void endLog() {
        limiter();
        System.out.println("\n");
    }

    private static RecMainLog getMainLogInfo() {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        StackTraceElement caller = Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(f -> f.getClassName().startsWith(APP_PKG))
                .filter(f -> !f.getClassName().contains(MyLog.class.getSimpleName())).findFirst().orElse(null);

        String fileName = (caller != null) ? caller.getFileName() : "unknown caller";
        String thread = Thread.currentThread().getName();

        return new RecMainLog(time, fileName, thread);
    }

    private record RecMainLog(String time, String fileName, String thread) {
    }

    public static void logHeader(String title) {
        var mainInfo = getMainLogInfo();

        System.out.printf("⏰ %s • 🗃️ %s • %s%n", mainInfo.time(), mainInfo.fileName(),
                title != null ? "📌 " + title : "🧵 " + mainInfo.thread());

    }

    public static void logTtl(String title, Object... arg) {

        startLog();
        logHeader(title);

        System.out.println("\t");

        if (arg != null)
            for (Object v : arg)
                System.out.println(v);

        endLog();
    }

    public static void log(Object... arg) {
        logTtl(null, arg);
    }

    public static void logKV(String key, Object val) {
        System.out.printf("🔑 %s => 🖍️ %s%n", key, val);
    }

    public static String logCols(List<String> cols) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cols.size(); i++) {
            sb.append(String.format("|%-1s%-20s", "", cols.get(i)));
            if ((i + 1) % 3 == 0)
                sb.append("\n");
        }

        return sb.toString();
    }

    public static void logErr(Throwable err) {
        wErr(err);

        startLog();

        if (err == null) {
            logTtl("⚠️ null error passed to logErr ⚠️");
            return;
        }

        logHeader(err.getMessage());

        System.out.println("\t");

        StackTraceElement[] frames = err.getStackTrace();

        for (StackTraceElement f : frames) {
            // if (f.toString().startsWith("server")) {
            System.out.printf("📂 %s => 🔢 %d | 🆎 %s | ☢️ %s%n", f.getFileName(), f.getLineNumber(), f.getMethodName(),
                    f.toString());
            // }
        }

        String msg = err.getMessage();
        int depth = frames.length;
        StackTraceElement last = depth > 0 ? frames[0] : null;

        System.out.println("\t");
        System.out.printf("📝 msg => %s%n", msg);
        System.out.printf("📏 depth => %d%n", depth);

        if (last != null) {
            System.out.printf("💥 last file => 📁 %s%n", last.getFileName());
            System.out.printf("📏 last line => %d%n", last.getLineNumber());
            System.out.printf("👻 last cb name => %s%n", last.getMethodName());
        }

        endLog();

    }

    public static void asyncLog(Path p, Object arg) {
        logThread.submit(() -> {
            try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                String json;
                if (arg instanceof Throwable err)
                    json = Prs.toJson(Map.of("msg", err.getMessage(), "type", err.getClass().getSimpleName(), "time",
                            LocalTime.now().toString()));
                else
                    json = Prs.toJson(arg);

                bw.write(json);
                bw.newLine();
            } catch (IOException err) {
                System.out.println("❌ failed log");
            }
        });
    }

    public static void wOk(Object arg) {
        asyncLog(Hiker.LOG_FILE, arg);
    }

    public static void wErr(Object arg) {
        asyncLog(Hiker.LOG_FILE_ERR, arg);
    }
}
