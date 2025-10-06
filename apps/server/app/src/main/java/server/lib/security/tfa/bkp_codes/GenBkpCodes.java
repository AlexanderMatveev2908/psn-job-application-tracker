package server.lib.security.tfa.bkp_codes;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.lib.data_structure.Prs;
import server.lib.security.hash.MyHashMng;
import server.lib.security.tfa.bkp_codes.etc.RecBkpCodes;

@Service @RequiredArgsConstructor
public class GenBkpCodes {

  private final static SecureRandom random = new SecureRandom();
  private final static int numberCodes = 8;

  private final MyHashMng hashMng;

  private String genPlainCode() {
    byte[] buf = new byte[4];
    random.nextBytes(buf);

    String hex = Prs.binaryToHex(buf).toUpperCase();

    return hex.substring(0, 4) + "-" + hex.substring(4);
  }

  private List<String> genCodes() {
    List<String> codes = new ArrayList<>();

    for (int i = 0; i < numberCodes; i++)
      codes.add(genPlainCode());

    return codes;
  }

  public Mono<RecBkpCodes> getCodes() {
    List<String> plainTxt = genCodes();

    return Flux.fromIterable(plainTxt).flatMap(txt -> hashMng.argonHash(txt)).collectList()
        .map(hashed -> new RecBkpCodes(plainTxt, hashed));
  }

}
