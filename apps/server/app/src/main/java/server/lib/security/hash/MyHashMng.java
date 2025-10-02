package server.lib.security.hash;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.lib.security.hash.hash_methods.DbHash;
import server.lib.security.hash.hash_methods.MyArgonHash;

@Service @RequiredArgsConstructor
public class MyHashMng {
  private final MyArgonHash myArgonHash;
  private final DbHash dbHash;

  public String hmacHash(String plain) {
    return dbHash.hash(plain);
  }

  public boolean hmacCheck(String hashed, String plain) {
    return dbHash.check(hashed, plain);
  }

  public Mono<String> argonHash(String plain) {
    return myArgonHash.rctHash(plain);
  }

  public Mono<Boolean> argonCheck(String hashed, String plain) {
    return myArgonHash.rctCheck(hashed, plain);
  }
}
