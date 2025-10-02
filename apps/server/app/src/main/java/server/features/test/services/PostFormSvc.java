package server.features.test.services;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.conf.cloud.CloudSvc;
import server.conf.cloud.etc.CloudAsset;
import server.decorators.AppFile;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostFormSvc {
  private final CloudSvc cloud;

  @SuppressWarnings("unchecked")
  public Mono<Tuple2<Integer, Integer>> postForm(Api api) {
    var form = api.getParsedForm().orElse(null);

    if (form == null)
      return Mono.error(new ErrAPI("no form data", 400));

    Set<String> assetKeys = Set.of("images", "videos");
    List<Mono<CloudAsset>> promises = new ArrayList<>();

    for (Map.Entry<String, Object> pair : form.entrySet()) {
      if (!assetKeys.contains(pair.getKey()))
        continue;

      var arg = (List<AppFile>) pair.getValue();
      for (AppFile f : arg) {
        if (!Files.exists(f.getFilePath()))
          throw new ErrAPI("file does not exist");

        promises.add(cloud.upload(f).doFinally(sig -> f.deleteLocally()));
      }
    }

    return Flux.merge(promises).collectList().zipWhen(saved -> Flux.fromIterable(saved)
        .flatMap(el -> cloud.delete(el.getPublicId(), el.getResourceType())).collectList()).map(tpl -> {
          List<CloudAsset> saved = tpl.getT1();
          List<Integer> deleted = tpl.getT2();

          int savedCount = saved.size();
          int deletedCount = deleted.stream().reduce(0, Integer::sum);

          return Tuples.of(savedCount, deletedCount);
        });
  }

}
