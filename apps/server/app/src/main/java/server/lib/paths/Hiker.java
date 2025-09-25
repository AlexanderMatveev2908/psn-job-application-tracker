package server.lib.paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import server.decorators.flow.ErrAPI;

public final class Hiker {

    public static final Path SERVER_DIR = Seeker.grabDir();
    public static final Path CERTS_DIR;
    public static final Path ASSETS_DIR;
    public static final Path IMAGES_DIR;
    public static final Path VIDEOS_DIR;
    public static final Path LOG_DIR;
    public static final Path LOG_FILE;
    public static final Path LOG_FILE_ERR;
    public static final Path CA_FILE;

    static {
        CERTS_DIR = SERVER_DIR.resolve("certs").normalize();
        ASSETS_DIR = SERVER_DIR.resolve("assets").normalize();
        IMAGES_DIR = ASSETS_DIR.resolve("images").normalize();
        VIDEOS_DIR = ASSETS_DIR.resolve("videos").normalize();
        LOG_DIR = SERVER_DIR.resolve("logger").normalize();
        LOG_FILE = LOG_DIR.resolve("log.json");
        LOG_FILE_ERR = LOG_DIR.resolve("log_err.json");
        CA_FILE = CERTS_DIR.resolve("supabase-ca.crt");

        ensureDirs();
    }

    private Hiker() {
        throw new ErrAPI("do not instantiate", 500);
    }

    private static void ensureDirs() {
        try {
            Files.createDirectories(CERTS_DIR);
            Files.createDirectories(IMAGES_DIR);
            Files.createDirectories(VIDEOS_DIR);
            Files.createDirectories(LOG_DIR);

            if (!Files.exists(LOG_FILE))
                Files.createFile(LOG_FILE);
            if (!Files.exists(LOG_FILE_ERR))
                Files.createFile(LOG_FILE_ERR);

            if (!Files.exists(CA_FILE))
                Files.createFile(CA_FILE);

        } catch (IOException e) {
            throw new ErrAPI("err generating required app dirs", 500);
        }
    }

    public static boolean existsDir() {
        try {
            return Files.isDirectory(CERTS_DIR)
                    && Files.isDirectory(IMAGES_DIR)
                    && Files.isDirectory(VIDEOS_DIR)
                    && Files.isDirectory(LOG_DIR)
                    && Files.isRegularFile(LOG_FILE)
                    && Files.isRegularFile(CA_FILE);
        } catch (Exception e) {
            return false;
        }
    }

}
