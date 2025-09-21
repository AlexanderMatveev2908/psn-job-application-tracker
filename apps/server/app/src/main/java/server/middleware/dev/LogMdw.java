package server.middleware.dev;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import server.decorators.AppFile;
import server.decorators.ErrAPI;
import server.decorators.flow.ReqAPI;
import server.lib.etc.Kit;

@Component
@Order(30)
@SuppressWarnings({ "unchecked" })
public class LogMdw implements Filter {

    private static final ExecutorService logThread = Executors.newSingleThreadExecutor();
    private final Kit kit;

    public LogMdw(Kit kit) {
        this.kit = kit;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ReqAPI reqAPI = (ReqAPI) req;
        Path loggerFile = kit.getHiker().getLogFile();

        Map<String, Object> arg = new LinkedHashMap<>();
        arg.put("url", reqAPI.getRequestURI());
        arg.put("method", reqAPI.getMethod());
        arg.put("accessToken", reqAPI.getHeader("authorization"));
        arg.put("refreshToken", extractRefreshToken(reqAPI));
        arg.put("query", normalizeEmpty(reqAPI.getQueryString()));
        arg.put("params", normalizeEmpty(reqAPI.getParameterMap()));
        arg.put("parsedQuery", normalizeEmpty((Map<String, Object>) reqAPI.getAttribute("parsedQuery")));
        arg.put("parsedForm", handleParsedForm(reqAPI));
        arg.put("body", handleBody(reqAPI));

        asyncLog(loggerFile, arg);
        chain.doFilter(reqAPI, res);
    }

    private String extractRefreshToken(ReqAPI reqAPI) {
        Cookie[] cookies = reqAPI.getCookies();
        if (cookies != null)
            for (Cookie c : cookies)
                if ("refreshToken".equals(c.getName()))
                    return c.getValue();

        return null;
    }

    private Object normalizeEmpty(Object obj) {
        if (obj == null)
            return null;
        if (obj instanceof String str && str.isBlank())
            return null;
        if (obj instanceof Map<?, ?> map && map.isEmpty())
            return null;
        return obj;
    }

    private Map<String, Object> handleParsedForm(ReqAPI reqAPI) {
        Map<String, Object> parsedForm = (Map<String, Object>) reqAPI.getAttribute("parsedForm");
        if (parsedForm == null || parsedForm.isEmpty())
            return null;

        Map<String, Object> cpyForm = parsedForm.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> newVal,
                        LinkedHashMap::new));

        List<AppFile> images = (List<AppFile>) cpyForm.get("images");
        List<AppFile> videos = (List<AppFile>) cpyForm.get("videos");

        if (images != null)
            cpyForm.put("images", images.stream().map(AppFile::getFancyShape).toList());

        if (videos != null)
            cpyForm.put("videos", videos.stream().map(AppFile::getFancyShape).toList());

        return cpyForm;
    }

    private Map<String, Object> handleBody(ReqAPI reqAPI) {
        String contentType = reqAPI.getContentType();
        if (contentType == null || contentType.startsWith("multipart/form-data"))
            return null;

        return reqAPI.grabBody();

    }

    private void asyncLog(Path loggerFile, Map<String, Object> arg) {
        logThread.submit(() -> {
            try (BufferedWriter bw = Files.newBufferedWriter(
                    loggerFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                String json = kit.getJack().writeValueAsString(arg);
                bw.write(json);
                bw.newLine();
            } catch (IOException err) {
                throw new ErrAPI("failed dev log", 500);
            }
        });
    }
}
