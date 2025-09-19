package server.middleware.parsers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import server.decorators.AppFile;
import server.decorators.ReqAPI;
import server.lib.etc.Hiker;

@SuppressWarnings("UseSpecificCatch")
@Component
@Order(0)
public class FormDataParser implements Filter {

    private static final ExecutorService fileExecutor = Executors.newFixedThreadPool(2);
    private final Path imagesDir = Hiker.grabDir().resolve("assets/images").normalize();

    public static String[] splitParts(ReqAPI reqAPI) {

        String contentType = reqAPI.getContentType();

        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            return null;
        }

        String boundary = "--" + contentType.split("boundary=")[1];
        byte[] rawBody = reqAPI.getRawBody();
        String txtBody = new String(rawBody, StandardCharsets.ISO_8859_1);
        String[] parts = txtBody.split(Pattern.quote(boundary));

        return parts;
    }

    public static String findPattern(String key, String headers) {
        Matcher m = Pattern.compile(String.format("%s=\"([^\"]+)\"", Pattern.quote(key))).matcher(headers);
        return !m.find() ? null
                : m.group(1);
    }

    public static AppFile handleImg(String body, String headers) {
        String filename = findPattern("filename", headers);

        if (filename == null)
            return null;

        String contentTypePart = null;
        Matcher cm = Pattern.compile("Content-Type: (.+)").matcher(headers);
        if (cm.find())
            contentTypePart = cm.group(1).trim();

        byte[] rawFile = body.getBytes(StandardCharsets.ISO_8859_1);

        return new AppFile("images", filename, contentTypePart, rawFile);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ReqAPI reqAPI = new ReqAPI((HttpServletRequest) req);
        String[] parts = splitParts(reqAPI);

        if (parts == null) {
            chain.doFilter(reqAPI, res);
            return;
        }

        StringBuilder sb = new StringBuilder();
        List<AppFile> images = new ArrayList<>();

        for (String prt : parts) {
            String[] headerAndBody = prt.split("\r\n\r\n", 2);
            if (headerAndBody.length < 2)
                continue;

            String headers = headerAndBody[0];
            String body = headerAndBody[1];

            String name = findPattern("name", headers);
            if (name == null)
                continue;

            if (headers.contains("filename=")) {
                AppFile img;
                if ((img = handleImg(body, headers)) != null) {

                    Files.createDirectories(imagesDir);
                    Files.createDirectories(imagesDir.resolve("../_").normalize());

                    Path imgPath = imagesDir.resolve(img.getFilename());

                    fileExecutor.submit(() -> {
                        try (OutputStream os = Files.newOutputStream(imgPath,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING)) {

                            os.write(img.getBts());

                            img.setFilePath(imgPath.toString());

                        } catch (Exception err) {
                        }
                    });

                    images.add(img);
                }
            } else {
                String val = new String(body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();

                sb.append(URLEncoder.encode(name, StandardCharsets.UTF_8));
                sb.append("=");
                sb.append(URLEncoder.encode(val, StandardCharsets.UTF_8));
                sb.append("&");
            }

        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        Map<String, Object> parsedForm = ParserManager.nestDict(sb.toString());
        parsedForm.put("images", images);
        reqAPI.setAttribute("parsedForm", parsedForm);

        chain.doFilter(reqAPI, res);
    }
}
