package server.conf.cloud;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.conf.cloud.etc.CloudAsset;
import server.conf.cloud.etc.CloudResourceT;
import server.conf.env_conf.EnvKeeper;
import server.decorators.AppFile;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;

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

    private String sign(String stringToSign) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sig = HexFormat.of().formatHex(digest);

            return sig;
        } catch (Exception err) {
            throw new ErrAPI("err creating cloud sign", 500);
        }
    }

    private String genSign(Map<String, String> params) {
        String cloudSecret = envKeeper.getCloudSecret();

        String stringToSign = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(el -> el.getKey() + "=" + el.getValue())
                .collect(Collectors.joining("&")) + cloudSecret;

        return sign(stringToSign);
    }

    private String getSignUpload(String tmsp, String folder, String publicId) {
        Map<String, String> params = new HashMap<>();
        params.put("folder", folder);
        params.put("timestamp", tmsp);
        params.put("public_id", publicId);

        return genSign(params);
    }

    private String getSignDelete(String tmsp, String publicId) {
        Map<String, String> params = new HashMap<>();
        params.put("timestamp", tmsp);
        params.put("public_id", publicId);

        return genSign(params);

    }

    public Mono<CloudAsset> upload(AppFile file) {
        String cloudKey = envKeeper.getCloudKey();
        String tmsp = String.valueOf(Instant.now().getEpochSecond());
        String folder = envKeeper.getAppName().replace("-", "_") + "__" + file.getField();

        String filename = file.getFilename();
        String publicId = filename.substring(0, filename.lastIndexOf('.'));

        String assetT = CloudResourceT.fromFileField(file.getField());
        var fileResource = assetT.equals("image") ? file.getResourceFromBts() : file.getResourceFromPath();
        String url = "/" + assetT + "/upload";

        MultipartBodyBuilder form = new MultipartBodyBuilder();
        form.part("api_key", cloudKey);
        form.part("signature", getSignUpload(tmsp, folder, publicId));
        form.part("timestamp", tmsp);
        form.part("folder", folder);
        form.part("public_id", publicId);
        form.part("file", fileResource);

        return getClient()
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form.build()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(str -> {

                    Map<String, Object> parsed = Frmt.toMap(str);

                    var asset = new CloudAsset((String) parsed.get("public_id"),
                            (String) parsed.get("secure_url"), (String) parsed.get("resource_type"));

                    return Mono.just(
                            asset);
                });
    }

    public Mono<Integer> delete(String publicId, String resourceType) {
        String cloudKey = envKeeper.getCloudKey();
        String tmsp = String.valueOf(Instant.now().getEpochSecond());

        MultipartBodyBuilder form = new MultipartBodyBuilder();
        form.part("api_key", cloudKey);
        form.part("public_id", publicId);
        form.part("timestamp", tmsp);
        form.part("signature", getSignDelete(tmsp, publicId));

        String url = "/" + resourceType + "/destroy";

        return getClient()
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form.build()))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    Map<String, Object> parsed = Frmt.toMap(response);

                    String result = parsed.get("result").toString();
                    int count = "ok".equals(result) ? 1 : 0;

                    System.out.println(String.format("✂️ deleted %d %s", count, resourceType));

                    return Mono.just(count);
                });
    }

}
