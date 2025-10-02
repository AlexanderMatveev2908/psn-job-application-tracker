package server.lib.security.hash.hash_methods;

import java.util.Arrays;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import server.decorators.flow.ErrAPI;

@Service
public final class MyArgonHash {
    private static final int POOL_SIZE = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);

    private static final Scheduler hashPool = Schedulers.fromExecutor(Executors.newFixedThreadPool(POOL_SIZE));

    private static final Argon2 ARGON = Argon2Factory.create();

    public String hash(String arg) {
        char[] splitted = arg.toCharArray();

        try {

            int iterations = 3;
            int memoryKB = 64 * 1024;

            String hash = ARGON.hash(iterations, memoryKB, POOL_SIZE, splitted);

            return hash;
        } finally {
            Arrays.fill(splitted, '\0');
        }
    }

    public boolean check(String hash, String txt) {
        char[] splitted = txt.toCharArray();

        try {
            return ARGON.verify(hash, splitted);
        } catch (Exception err) {
            throw new ErrAPI("invalid pwd", 401);
        } finally {
            Arrays.fill(splitted, '\0');
        }
    }

    public Mono<String> rctHash(String arg) {
        return Mono.fromCallable(() -> hash(arg)).subscribeOn(hashPool);
    }

    public Mono<Boolean> rctCheck(String hash, String txt) {
        return Mono.fromCallable(() -> check(hash, txt)).subscribeOn(hashPool);
    }
}
