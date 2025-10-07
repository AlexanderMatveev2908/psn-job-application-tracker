package server.features.test.services.get_user_test;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import server.decorators.flow.api.Api;
import server.features.test.paperwork.UserTestForm;
import server.features.test.services.get_user_test.etc.RecUserTest;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.tokens.cbc_hmac.MyCbcHmac;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.lib.security.mng_tokens.tokens.jwe.MyJwe;
import server.lib.security.mng_tokens.tokens.jwe.etc.RecCreateJweReturnT;
import server.lib.security.mng_tokens.tokens.jwt.MyJwt;
import server.lib.security.tfa.My2FA;
import server.middleware.form_checkers.FormChecker;
import server.models.backup_code.svc.BkpCodesRepo;
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
  private final BkpCodesRepo bkpCodesRepo;
  private final FormChecker formCk;
  private final My2FA tfa;

  public Mono<Map<String, Object>> getUserTest(Api api) {
    Map<String, Object> query = api.getParsedQuery().orElse(Map.of("tokenT", "conf_email"));

    TokenT tokenT = (query.get("tokenT") instanceof String str) ? TokenT.fromAny(str) : TokenT.CONF_EMAIL;

    Set<String> expiredList = (query.get("expired") instanceof List<?> argExp)
        ? argExp.stream().map(Object::toString).collect(Collectors.toSet())
        : Set.of();

    boolean verifyUser = Boolean.valueOf((query.get("verifyUser") instanceof String val ? val : ""));
    boolean use2FA = Boolean.valueOf((query.get("use2FA") instanceof String val ? val : ""));

    return getInst(api, verifyUser, use2FA).flatMap(rec -> handleTokens(rec, tokenT, expiredList));
  }

  private Mono<RecUserTest> getInst(Api api, boolean verifyUser, boolean use2FA) {
    return getPayload(api).flatMap(userPayload -> {

      return userRepo.findByEmail(userPayload.getEmail()).flatMap(dbUser -> {
        return tokenRepo.delByUserId(dbUser.getId()).collectList().flatMap(ids -> {

          // ! if u sent a plain text will have again plain text
          // ! else if you do not need it
          // ! you can just send user saved from db
          // ! password will not be checked in this service
          String plainPwd = userPayload.getPassword();

          return Mono.zip(Mono.just(dbUser), Mono.just(plainPwd))
              .flatMap(tpl -> !dbUser.use2FA() && use2FA ? manage2FA(tpl) : Mono.just(RecUserTest.fromTpl(tpl)));
        });
      }).switchIfEmpty(createUser(userPayload, verifyUser, use2FA));
    });
  }

  private Mono<RecUserTest> createUser(User payloadUser, boolean verifyUser, boolean use2FA) {

    var plainPwd = payloadUser.getPassword();

    return hashMng.argonHash(payloadUser.getPassword()).flatMap(hashed -> {
      payloadUser.setPassword(hashed);

      return Mono.zip(

          userRepo.insert(payloadUser)
              .flatMap(dbUser -> verifyUser || use2FA ? userRepo.verifyUser(dbUser.getId()) : Mono.<User>just(dbUser)),
          Mono.just(plainPwd))

          .flatMap(tpl -> use2FA ? manage2FA(tpl) : Mono.just(RecUserTest.fromTpl(tpl)));
    });
  }

  // ? 1 -> user; 2 -> his plain pwd
  private Mono<RecUserTest> manage2FA(Tuple2<User, String> tpl) {
    var user = tpl.getT1();

    return tfa.setup2FA(user)
        .flatMap(rec -> userRepo.setTotpSecret(rec.recTOTP().encrypted(), user.getId())
            .flatMap(updatedUser -> Flux.fromIterable(rec.recBkpCodes().hashed())
                .flatMap(code -> bkpCodesRepo.insert(user.getId(), code)).collectList()
                .then(Mono.just(new RecUserTest(updatedUser, tpl.getT2(), rec)))));

  }

  @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
  private Mono<User> getPayload(Api api) {
    var defUser = new User(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress(),
        "8cLS4XY!{2Wdvl4*l^4");

    return api.getBd(new TypeReference<Map<String, Object>>() {
    }).map(body -> {
      if (body.get("existingPayload") != null && body.get("existingPayload") instanceof Map userMap) {
        var form = UserTestForm.fromMap(userMap);
        formCk.checkForm(form);
        return User.fromTestPayload(userMap);
      }

      return defUser;
    }).defaultIfEmpty(defUser);
  }

  private Mono<Map<String, Object>> handleTokens(RecUserTest rec, TokenT tokenT, Set<String> expiredList) {

    User dbUser = rec.user();
    String plainPwd = rec.plainPwd();

    String jwt = myJwt.create(dbUser.getId(), expiredList.contains("jwt"));
    RecCreateJweReturnT recJwe = myJwe.create(dbUser.getId(), expiredList.contains("jwe"));
    RecCreateCbcHmacReturnT recCbcHmac = myCbcHmac.create(tokenT, dbUser.getId(), expiredList.contains("cbc_hmac"));

    return Mono.zip(tokenRepo.insert(recJwe.inst()), tokenRepo.insertWithId(recCbcHmac.inst())).map(tplTokens -> {

      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("user", dbUser);
      payload.put("plainPwd", plainPwd);

      var rec2FA = rec.rec2FA();

      if (rec2FA != null) {
        payload.put("totpSecret", rec2FA.recTOTP().clientSecret());
        payload.put("bkpCodes", rec2FA.recBkpCodes().clientCodes());
      }

      payload.put("accessToken", jwt);
      payload.put("refreshToken", recJwe.clientToken());
      payload.put("cbcHmacToken", recCbcHmac.clientToken());

      return payload;
    });
  }
}
