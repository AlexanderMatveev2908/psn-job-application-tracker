package server.conf.cloud;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.env_conf.EnvKeeper;
import server.decorators.AppFile;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.dev.MyLog;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class CloudSvc {
    private final WebClient.Builder webClientBuilder;
    private final EnvKeeper envKeeper;

    private WebClient getClient() {
        String cloudName = envKeeper.getCloudName();

        return webClientBuilder
                .baseUrl("https://api.cloudinary.com/v1_1/" + cloudName)
                .build();
    }

    private String getSign(String tmsp, String folder, String publicId) {
        String cloudSecret = envKeeper.getCloudSecret();

        Map<String, String> paramsToSign = new TreeMap<>();
        paramsToSign.put("folder", folder);
        paramsToSign.put("timestamp", tmsp);
        paramsToSign.put("public_id", publicId);

        String stringToSign = paramsToSign.entrySet().stream()
                .map(el -> el.getKey() + "=" + el.getValue())
                .collect(Collectors.joining("&")) + cloudSecret;

        String sig;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(stringToSign.getBytes(StandardCharsets.UTF_8));
            sig = HexFormat.of().formatHex(digest);
        } catch (Exception err) {
            throw new ErrAPI("err creating cloud sign", 500);
        }

        return sig;

    }

    public Mono<String> upload(AppFile file) {
        String cloudKey = envKeeper.getCloudKey();
        String tmsp = String.valueOf(Instant.now().getEpochSecond());
        String folder = envKeeper.getAppName().replace("-", "_") + "__" + file.getField();
        String filename = file.getFilename();
        String publicId = filename.substring(0, filename.lastIndexOf('.'));

        MultipartBodyBuilder form = new MultipartBodyBuilder();
        form.part("api_key", cloudKey);
        form.part("signature", getSign(tmsp, folder, publicId));
        form.part("timestamp", tmsp);
        form.part("folder", folder);
        form.part("public_id", publicId);
        form.part("file", file.getResourceFromBts());

        return getClient()
                .post()
                .uri("/image/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form.build()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(res -> {
                    var parsed = Frmt.toMap(res);
                    MyLog.wLogOk((parsed));
                })
                .doOnError(err -> System.out.println("❌ cloud err: " + err.getMessage()));
    }

}
