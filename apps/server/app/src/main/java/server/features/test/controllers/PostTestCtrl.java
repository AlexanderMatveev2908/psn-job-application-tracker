package server.features.test.controllers;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.conf.cloud.CloudSvc;
import server.conf.cloud.etc.CloudAsset;
import server.decorators.AppFile;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.lib.data_structure.ShapeCheck;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class PostTestCtrl {

    private final CloudSvc cloud;

    public Mono<ResponseEntity<ResAPI>> postMsg(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        })
                .flatMap(bd -> {
                    var msg = (String) bd.get("msg");

                    if (!ShapeCheck.isStr(msg))
                        return new ResAPI(400).msg("missing msg").build();

                    return new ResAPI(200).msg("msg received").data(Map.of("clientMsg", msg)).build();
                });
    }

    @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch" })
    public Mono<ResponseEntity<ResAPI>> postFormData(Api api) {

        var form = api.getParsedForm().orElse(null);

        if (form == null)
            return new ResAPI(400).msg("no form data").build();

        Set<String> assetKeys = Set.of("images", "videos");

        List<Mono<CloudAsset>> promises = new ArrayList<>();

        for (Map.Entry<String, Object> pair : form.entrySet()) {
            if (!assetKeys.contains(pair.getKey()))
                continue;

            var arg = (List<AppFile>) pair.getValue();

            for (AppFile f : arg) {

                if (!Files.exists(f.getFilePath()))
                    throw new ErrAPI("file does not exist");

                promises.add(cloud.upload(f).doFinally(sig -> {
                    f.deleteLocally();
                }));

            }
        }

        return Flux.merge(promises)
                .collectList()
                .zipWhen(saved -> Flux.fromIterable(saved)
                        .flatMap(el -> cloud.delete(el.getPublicId(), el.getResourceType()))
                        .collectList())
                .flatMap(tuple -> {
                    List<CloudAsset> saved = tuple.getT1();
                    List<Integer> deleted = tuple.getT2();

                    return new ResAPI(200).msg(
                            "form parsed • processed • saved locally • uploaded on cloud • deleted locally • deleted from cloud")
                            .data(
                                    Map.of("saved", saved, "deleted",
                                            deleted.stream().reduce(0, (acc, curr) -> acc + curr)))
                            .build();
                });

    }

}
