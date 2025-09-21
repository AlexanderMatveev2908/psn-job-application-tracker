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
import server.decorators.ErrAPI;
import server.decorators.flow.ReqAPI;
import server.lib.etc.Kit;
import server.lib.paths.Hiker;

class CtxParse {
    StringBuilder sb = new StringBuilder();
    List<AppFile> images = new ArrayList<>();
    List<AppFile> videos = new ArrayList<>();
    final Hiker hiker;

    CtxParse(Hiker hiker) {
        this.hiker = hiker;
    }
}

class PartCtx {
    final String headers;
    final String body;
    final String name;

    PartCtx(String headers, String body) {
        this.headers = headers;
        this.body = body;
        this.name = FormDataParser.findPattern("name", headers);
    }
}

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

        CtxParse ctx = new CtxParse(kit.getHiker());

        for (String prt : parts)
            handlePart(prt, ctx);

        if (ctx.sb.length() > 0)
            ctx.sb.setLength(ctx.sb.length() - 1);

        Map<String, Object> parsedForm = ParserManager.nestDict(ctx.sb.toString());
        parsedForm.put("images", ctx.images);
        parsedForm.put("videos", ctx.videos);
        reqAPI.setAttribute("parsedForm", parsedForm);

        chain.doFilter(reqAPI, res);
    }

    private void handlePart(String prt, CtxParse ctx) {
        String[] headerAndBody = prt.split("\r\n\r\n", 2);
        if (headerAndBody.length < 2)
            return;

        PartCtx part = new PartCtx(headerAndBody[0], headerAndBody[1]);
        if (part.name == null)
            return;

        if (part.headers.contains("filename=")) {
            handleAssetPart(part, ctx);
        } else {
            String val = new String(part.body.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).trim();
            ctx.sb.append(URLEncoder.encode(part.name, StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(val, StandardCharsets.UTF_8))
                    .append("&");
        }
    }

    private void handleAssetPart(PartCtx part, CtxParse ctx) {
        if (!Set.of("images", "videos").contains(part.name))
            return;

        boolean isImage = part.name.equals("images");
        AppFile asset = handleAsset(part.body, part.headers, part.name);
        if (asset == null)
            return;

        Path dir = isImage ? ctx.hiker.getImagesDir() : ctx.hiker.getVideosDir();
        Path assetPath = dir.resolve(asset.getFilename());

        fileExecutor.submit(() -> saveAsset(asset, assetPath));

        if (isImage)
            ctx.images.add(asset);
        else
            ctx.videos.add(asset);
    }

    private void saveAsset(AppFile asset, Path assetPath) {
        try (OutputStream os = Files.newOutputStream(assetPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            os.write(asset.getBts());
            asset.setFilePath(assetPath.toString());
        } catch (IOException err) {
            throw new ErrAPI("err saving asset locally", 500);
        }
    }

}