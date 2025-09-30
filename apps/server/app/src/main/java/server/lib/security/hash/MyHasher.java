package server.lib.security.hash;

import java.util.Arrays;
import java.util.concurrent.Executors;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import server.decorators.flow.ErrAPI;

public final class MyHasher {
    private static final int POOL_SIZE = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);

    private static final Scheduler hashPool = Schedulers.fromExecutor(Executors.newFixedThreadPool(POOL_SIZE));

    private static final Argon2 ARGON = Argon2Factory.create();

    public MyHasher() {
        throw new ErrAPI("MyHash should be a static lib only");
    }

    public static String hash(String arg) {
        char[] splitted = arg.toCharArray();

        try {

            int iterations = 3;
            int memoryKB = 64 * 1024;

            String hash = ARGON.hash(iterations, memoryKB, POOL_SIZE,
                    splitted);

            return hash;
        } finally {
            Arrays.fill(splitted, '\0');
        }
    }

    public static boolean check(String hash, String txt) {
        char[] splitted = txt.toCharArray();

        try {
            return ARGON.verify(hash, splitted);
        } catch (Exception err) {
            throw new ErrAPI("invalid pwd", 401);
        } finally {
            Arrays.fill(splitted, '\0');
        }
    }

    public static Mono<String> rctHash(String arg) {
        return Mono.fromCallable(() -> hash(arg)).subscribeOn(hashPool);
    }

    public static Mono<Boolean> rctCheck(String hash, String txt) {
        return Mono.fromCallable(() -> check(hash, txt)).subscribeOn(hashPool);
    }
}
