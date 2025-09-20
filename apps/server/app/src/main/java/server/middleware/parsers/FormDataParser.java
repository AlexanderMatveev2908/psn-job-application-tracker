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
import java.util.Set;
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
import server.decorators.flow.ReqAPI;
import server.lib.etc.Kit;
import server.lib.paths.Hiker;

@SuppressWarnings("UseSpecificCatch")
@Component
@Order(10)
public class FormDataParser implements Filter {

    private static final ExecutorService fileExecutor = Executors.newFixedThreadPool(2);
    private final Kit kit;

    public FormDataParser(Kit kit) {
        this.kit = kit;
    }

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

    public static AppFile handleAsset(String body, String headers, String field) {
        String filename = findPattern("filename", headers);

        if (filename == null)
            return null;

        String contentTypePart = null;
        Matcher cm = Pattern.compile("Content-Type: (.+)").matcher(headers);
        if (cm.find())
            contentTypePart = cm.group(1).trim();

        byte[] rawFile = body.getBytes(StandardCharsets.ISO_8859_1);

        return new AppFile(field, filename, contentTypePart, rawFile);
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
        List<AppFile> videos = new ArrayList<>();
        Hiker hiker = kit.getHiker();
        Path imagesDir = hiker.getImagesDir();
        Path videosDir = hiker.getVideosDir();

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

                if (!Set.of("images", "videos").contains(name))
                    continue;

                boolean isImage = name.equals("images");

                AppFile asset;
                if ((asset = handleAsset(body, headers, name)) != null) {

                    String generatedFilename = asset.getFilename();
                    Path assetPath = isImage ? imagesDir.resolve(generatedFilename)
                            : videosDir.resolve(generatedFilename);

                    fileExecutor.submit(() -> {
                        try (OutputStream os = Files.newOutputStream(assetPath,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING)) {

                            os.write(asset.getBts());

                            asset.setFilePath(assetPath.toString());

                        } catch (Exception err) {
                        }
                    });

                    if (isImage)
                        images.add(asset);
                    else
                        videos.add(asset);
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
        parsedForm.put("videos", videos);
        reqAPI.setAttribute("parsedForm", parsedForm);

        chain.doFilter(reqAPI, res);
    }
}
