package server.lib.dev;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.db.database.DB;
import server.conf.db.remote_dictionary.RdCmd;
import server.lib.security.tfa.totp.MyTotp;

@SuppressWarnings({ "unused", "UseSpecificCatch", "CallToPrintStackTrace" }) @SuppressFBWarnings({
        "EI2" }) @Service @RequiredArgsConstructor
public class Dev {
    // private final RdCmd cmd;
    private final DB db;
    private final RdCmd cmd;
    private final MyTotp totp;

    // @Bean
    // public ApplicationRunner logRoutes(RequestMappingHandlerMapping mapping) {
    // return args -> {
    // mapping.getHandlerMethods().forEach((k, v) -> {
    // System.out.println("📡 " + k + " => " + v);
    // });
    // };
    // }

    public void mng() {
        // try {

        // var extracted = totp.plainB32FromEncHex(
        // "fdc7ec429c1cd966d8c4e980a903ede472370d62e5f007c7195d6dcf82c094b82c46bf1db35fa8935f6ef595a5665b3f24ab594d6a94566b36cba54c");
        // var original = "YD5IRLBFP34FNPFGXHRHNJWMXV2KFXOQ";

        // MyLog.log(extracted, original, extracted.equals(original));

        // MyLog.log(totp.checkTotp(
        // "fdc7ec429c1cd966d8c4e980a903ede472370d62e5f007c7195d6dcf82c094b82c46bf1db35fa8935f6ef595a5665b3f24ab594d6a94566b36cba54c",
        // 429673));
        // } catch (Exception err) {
        // MyLog.logErr(err);
        // }
    }

    public void dropAll() {
        db.truncateAll().flatMap(count -> {
            return cmd.flushAll();
        }).subscribe();
    }

}
