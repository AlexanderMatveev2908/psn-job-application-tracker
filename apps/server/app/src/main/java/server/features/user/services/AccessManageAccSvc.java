package server.features.user.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.models.backup_code.etc.RecInfoBkp;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenCombo;
import server.models.token.svc.TokenRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class AccessManageAccSvc {

  private final TokenCombo tkCombo;
  private final TokenRepo tokenRepo;
  private final BkpCodesRepo bkpRepo;

  public Mono<String> simpleAccess(Api api) {
    var user = api.getUser();
    var tokenT = user.use2FA() ? TokenT.MANAGE_ACC_2FA : TokenT.MANAGE_ACC;

    return tkCombo.insertCbcHmac(user.getId(), tokenT);
  }

  public Mono<String> access2FA(Api api) {
    var user = api.getUser();

    Optional<RecInfoBkp> recBkp = api.getInfoBkp();

    return tokenRepo.delByUserIdAndTokenT(user.getId(), TokenT.MANAGE_ACC_2FA)
        .then(tkCombo.insertCbcHmac(user.getId(), TokenT.MANAGE_ACC).flatMap(clientToken -> {
          return (recBkp.isPresent() ? bkpRepo.delById(recBkp.get().bkpMatch().getId()).collectList() : Mono.empty())
              .thenReturn(clientToken);
        }));
  }
}
