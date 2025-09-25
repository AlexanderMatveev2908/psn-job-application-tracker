package server.conf.cloud;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.env_conf.EnvKeeper;
import server.decorators.AppFile;

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

    public Mono<String> upload(AppFile file) {
        String cloudKey = envKeeper.getCloudKey();
        String cloudSecret = envKeeper.getCloudSecret();
        String tmsp = String.valueOf(Instant.now().getEpochSecond());

        String sigBase = "timestamp=" + tmsp + cloudSecret;

        String sig = DigestUtils.appendMd5DigestAsHex(
                sigBase.getBytes(StandardCharsets.UTF_8),
                new StringBuilder()).toString();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("api_key", cloudKey);
        bodyBuilder.part("signature", sig);
        bodyBuilder.part("timestamp", tmsp);
        bodyBuilder.part("folder", envKeeper.getAppName().replace("-", "_") + "__" +
                file.getField());
        bodyBuilder.part("file", file.getResourceFromBts());

        return getClient().post()
                .uri("/image/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(res -> System.out.println("📸 res cloud" + res)).doOnError(err -> {
                    System.out.println(err);
                });
    }
}
