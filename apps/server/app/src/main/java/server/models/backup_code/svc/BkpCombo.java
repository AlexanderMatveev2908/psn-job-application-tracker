package server.models.backup_code.svc;

import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.backup_code.etc.RecInfoBkp;

@Service @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class BkpCombo {

  private final BkpCodesRepo bkpRepo;

  public Mono<Void> delMatchIfUsed(Api api) {
    Optional<RecInfoBkp> rec = api.getInfoBkp();
    return rec.isPresent() ? bkpRepo.delById(rec.get().bkpMatch().getId()).then() : Mono.empty();
  }
}
