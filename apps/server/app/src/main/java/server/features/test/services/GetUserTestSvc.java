package server.features.test.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.Api;
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
  private static final Faker faker = new Faker();

  private final TokenRepo tokenRepo;
  private final UserRepo userRepo;
  private final MyHashMng hashMng;
  private final MyJwt myJwt;
  private final MyJwe myJwe;
  private final MyCbcHmac myCbcHmac;

  public Mono<Map<String, Object>> getUserTest(Api api) {
    Map<String, Object> query = api.getParsedQuery().orElse(Map.of("tokenT", "conf_email"));

    TokenT tokenT = (query.get("tokenT") instanceof String str) ? TokenT.fromAny(str) : TokenT.CONF_EMAIL;

    Set<String> expiredList = (query.get("expired") instanceof List<?> argExp)
        ? argExp.stream().map(Object::toString).collect(Collectors.toSet())
        : Set.of();

    return getInst(api).flatMap(tpl -> handleTokens(tpl, tokenT, expiredList));
  }

  @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
  private Mono<Tuple2<User, String>> getInst(Api api) {
    return api.getBd(new TypeReference<Map<String, Object>>() {
    }).flatMap(body -> {
      var existingPayload = (Map<String, Object>) body.get("existingPayload");

      if (existingPayload == null)
        return createUser();

      User us = User.fromTestPayload(existingPayload);

      return userRepo.findByEmail(us.getEmail()).flatMap(dbUser -> {

        return tokenRepo.delByUserId(dbUser.getId()).collectList().flatMap(ids -> {
          System.out.println("🧹 tokens deleted deleted => " + ids.size());

          String plainPwd = "N/A";
          if (existingPayload.get("plainPwd") instanceof String plainPwdStr)
            plainPwd = plainPwdStr;

          return Mono.zip(Mono.just(dbUser), Mono.just(plainPwd));
        });
      }).switchIfEmpty(Mono.defer(() -> createUser()));
    }).switchIfEmpty(createUser());
  }

  private Mono<Tuple2<User, String>> createUser() {
    var plainPwd = "8cLS4XY!{2Wdvl4*l^4";
    var us = new User(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(), plainPwd);

    return hashMng.argonHash(us.getPassword()).flatMap(hashed -> {
      us.setPassword(hashed);
      return Mono.zip(userRepo.insert(us), Mono.just(plainPwd));
    });
  }

  private Mono<Map<String, Object>> handleTokens(Tuple2<User, String> tpl, TokenT tokenT, Set<String> expiredList) {

    User dbUser = tpl.getT1();
    String plainPwd = tpl.getT2();
    String jwt = myJwt.create(dbUser.getId(), expiredList.contains("jwt"));
    RecCreateJweReturnT recJwe = myJwe.create(dbUser.getId(), expiredList.contains("jwe"));
    RecCreateCbcHmacReturnT recCbcHmac = myCbcHmac.create(tokenT, dbUser.getId(), expiredList.contains("cbc_hmac"));

    return Mono.zip(tokenRepo.insert(recJwe.inst()), tokenRepo.insertWithId(recCbcHmac.inst())).map(tplTokens -> {
      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("user", dbUser);
      payload.put("plainPwd", plainPwd);
      payload.put("accessToken", jwt);
      payload.put("refreshToken", recJwe.clientToken());
      payload.put("cbcHmacToken", recCbcHmac.clientToken());
      payload.put("refreshTokenDb", tplTokens.getT1());
      payload.put("cbcHmacTokenDb", tplTokens.getT2());
      return payload;
    });
  }
}
