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
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.AppFile;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.etc.Kit;

@Component
@Order(100)
@SuppressWarnings({ "unchecked" })
public class LogMdw implements WebFilter {

    private static final ExecutorService logThread = Executors.newSingleThreadExecutor();
    private final Kit kit;

    public LogMdw(Kit kit) {
        this.kit = kit;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        Api api = (Api) exc;
        Path loggerFile = kit.getHiker().getLogFile();

        Map<String, Object> arg = new LinkedHashMap<>();
        arg.put("url", api.getPath());
        arg.put("method", api.getMethod().toString());
        arg.put("accessToken", normalizeEmpty(api.getHeader("authorization")));
        arg.put("refreshToken", normalizeEmpty(api.getCookie("refreshToken")));
        arg.put("query", normalizeEmpty(api.getQuery()));
        arg.put("parsedQuery", api.getParsedQuery().orElse(null));
        arg.put("parsedForm", handleParsedForm(api));

        return api.getBdStr()
                .defaultIfEmpty("")
                .doOnNext(body -> {

                    arg.put("body", api.getContentType().contains("multipart/form-data") ? null : normalizeEmpty(body));

                    asyncLog(loggerFile, arg);
                })
                .then(chain.filter(api));

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

    private Map<String, Object> handleParsedForm(Api api) {
        var parsedForm = api.getParsedForm().orElse(null);
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
