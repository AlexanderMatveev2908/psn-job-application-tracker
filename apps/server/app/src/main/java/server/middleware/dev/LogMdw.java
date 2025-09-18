package server.middleware.dev;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;

@SuppressWarnings({ "UseSpecificCatch", "unchecked" })
@Component
@Order(10)
public class LogMdw implements Filter {

    private static final ExecutorService logThread = Executors.newSingleThreadExecutor();
    private static final ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ReqAPI reqAPI = (ReqAPI) req;

        Path appDir = Path.of(System.getProperty("user.dir"));
        Path serverDir = appDir.resolve("../").normalize();
        Path loggerDir = serverDir.resolve("logger").normalize();
        Path loggerFile = loggerDir.resolve("log.json").normalize();

        Files.createDirectories(loggerDir);
        if (Files.notExists(loggerFile))
            Files.createFile(loggerFile);

        Map<String, Object> arg = new LinkedHashMap<>();

        arg.put("url", reqAPI.getRequestURI());
        arg.put("method", reqAPI.getMethod());

        String query = reqAPI.getQueryString();
        arg.put("query", (query == null || query.isBlank()) ? null : query);

        Map<String, String[]> params = reqAPI.getParameterMap();
        arg.put("params", params.isEmpty() ? null : params);

        Map<String, Object> parsedQuery = (Map<String, Object>) reqAPI.getAttribute("parsedQuery");
        arg.put("parsedQuery", parsedQuery == null || parsedQuery.isEmpty() ? null : parsedQuery);

        Map<String, Object> parsedForm = (Map<String, Object>) reqAPI.getAttribute("parsedForm");
        arg.put("parsedForm", parsedForm == null || parsedForm.isEmpty() ? null : parsedForm);

        String accessToken = reqAPI.getHeader("authorization");
        arg.put("accessToken", accessToken);

        Cookie[] cookies = reqAPI.getCookies(); // may be null if no cookies
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    refreshToken = c.getValue();
                    break;
                }
            }
        }
        arg.put("refreshToken", refreshToken);

        String contentType = reqAPI.getContentType();
        Map<String, Object> body = null;
        if (!contentType.startsWith("multipart/form-data")) {
            try {
                body = reqAPI.grabBody();
            } catch (Exception err) {
            }
        }

        arg.put("body", body);

        logThread.submit(() -> {
            try (BufferedWriter bw = Files.newBufferedWriter(
                    loggerFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                String json = jack.writeValueAsString(arg);

                bw.write(json);
                bw.newLine();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        chain.doFilter(reqAPI, res);
    }
}
