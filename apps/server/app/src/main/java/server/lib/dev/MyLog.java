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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.paths.Hiker;

public class MyLog {

    private static final String APP_PKG = "server";
    private static final ExecutorService logThread = Executors.newSingleThreadExecutor();

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

    public static void logKV(String key, Object val) {
        System.out.printf("🔑 %s => 🖍️ %s%n", key, val);
    }

    public static void logCols(List<String> cols) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cols.size(); i++) {
            sb.append(String.format("|%-1s%-20s", "", cols.get(i)));
            if ((i + 1) % 3 == 0)
                sb.append("\n");
        }
        if (cols.size() % 3 != 0)
            sb.append("\n");

        System.out.println(sb.toString());
    }

    public static void logErr(Throwable err) {
        wErr(err);

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

        System.out.println("\t");
    }

    public static void asyncLog(Path p, Object arg) {

        logThread.submit(() -> {

            try (BufferedWriter bw = Files.newBufferedWriter(
                    p,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                String json = Frmt.toJson(arg);

                bw.write(json);
                bw.newLine();
            } catch (IOException err) {

                System.out.println("❌ err writing log.json file");

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
