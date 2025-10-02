package server.features.test.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Prs;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.tokens.cbc_hmac.MyCbcHmac;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.lib.security.mng_tokens.tokens.jwe.MyJwe;
import server.lib.security.mng_tokens.tokens.jwe.etc.RecCreateJweReturnT;
import server.lib.security.mng_tokens.tokens.jwt.MyJwt;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class GetUserTestSvc {
  private final static Faker faker = new Faker();
  private final TokenRepo tokenRepo;
  private final UserRepo userRepo;
  private final MyHashMng hashMng;
  private final MyJwt myJwt;
  private final MyJwe myJwe;
  private final MyCbcHmac myCbcHmac;

  public Mono<Map<String, Object>> getUserTest(Api api) {
    var us = new User(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(),
        "8cLS4XY!{2Wdvl4*l^4");

    Map<String, Object> query = api.getParsedQuery().orElseThrow(() -> new ErrAPI("missing query params", 400));

    TokenT tokenT = TokenT.fromAny(query.get("tokenT"));

    Set<String> expiredList = (query.get("expired") instanceof List<?> argExp)
        ? argExp.stream().map(Object::toString).collect(Collectors.toSet())
        : Set.of();

    return hashMng.argonHash(us.getPassword()).flatMap(hashed -> {
      us.setPassword(hashed);

      return userRepo.insert(us).flatMap(dbUser -> {

        String jwt = myJwt.create(dbUser.getId(), expiredList.contains("jwt"));
        RecCreateJweReturnT recJwe = myJwe.create(dbUser.getId(), expiredList.contains("jwe"));
        RecCreateCbcHmacReturnT recCbcHmac = myCbcHmac.create(tokenT, dbUser.getId(), expiredList.contains("cbc_hmac"));

        return Mono.zip(tokenRepo.insert(recJwe.inst()), tokenRepo.insertWithId(recCbcHmac.inst())).map(tpl -> {
          return Prs.linkedMap("accessToken", jwt, "refreshToken", recJwe.clientToken(), "cbcHmacToken",
              recCbcHmac.clientToken(), "refreshTokenDb", tpl.getT1(), "cbcHmacTokenDb", tpl.getT2());
        });
      });
    });
  }

}
