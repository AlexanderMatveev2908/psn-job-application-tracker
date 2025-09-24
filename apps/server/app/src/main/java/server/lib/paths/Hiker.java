package server.lib.paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import server.decorators.flow.ErrAPI;

public final class Hiker {

    private static final Path SERVER_DIR = Seeker.grabDir();
    private static final Path CERTS_DIR;
    private static final Path ASSETS_DIR;
    private static final Path IMAGES_DIR;
    private static final Path VIDEOS_DIR;
    private static final Path LOG_DIR;
    private static final Path LOG_FILE;
    private static final Path CA_FILE;

    static {
        CERTS_DIR = SERVER_DIR.resolve("certs").normalize();
        ASSETS_DIR = SERVER_DIR.resolve("assets").normalize();
        IMAGES_DIR = ASSETS_DIR.resolve("images").normalize();
        VIDEOS_DIR = ASSETS_DIR.resolve("videos").normalize();
        LOG_DIR = SERVER_DIR.resolve("logger").normalize();
        LOG_FILE = LOG_DIR.resolve("log.json");
        CA_FILE = CERTS_DIR.resolve("supabase-ca.crt");

        ensureDirs();
    }

    private static void ensureDirs() {
        try {
            Files.createDirectories(CERTS_DIR);
            Files.createDirectories(IMAGES_DIR);
            Files.createDirectories(VIDEOS_DIR);
            Files.createDirectories(LOG_DIR);

            if (!Files.exists(LOG_FILE))
                Files.createFile(LOG_FILE);

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

    public static Path getServerDir() {
        return SERVER_DIR;
    }

    public static Path getAssetsDir() {
        return ASSETS_DIR;
    }

    public static Path getImagesDir() {
        return IMAGES_DIR;
    }

    public static Path getVideosDir() {
        return VIDEOS_DIR;
    }

    public static Path getLogDir() {
        return LOG_DIR;
    }

    public static Path getLogFile() {
        return LOG_FILE;
    }

    public static Path getCertsDir() {
        return CERTS_DIR;
    }

    public static Path getCaFile() {
        return CA_FILE;
    }
}
