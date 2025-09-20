package server.lib.paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class Hiker {
    private final Path serverDir = Seeker.grabDir();
    private final Path certsDir;
    private final Path imagesDir;
    private final Path videosDir;
    private final Path logDir;
    private final Path logFile;

    public Hiker() {
        this.imagesDir = serverDir.resolve("assets/images").normalize();
        this.videosDir = serverDir.resolve("assets/videos").normalize();
        this.certsDir = serverDir.resolve("certs").normalize();
        this.logDir = serverDir.resolve("logger").normalize();
        this.logFile = logDir.resolve("log.json");

        ensureDirs();
    }

    private void ensureDirs() {
        try {
            Files.createDirectories(certsDir);
            Files.createDirectories(imagesDir);
            Files.createDirectories(videosDir);
            Files.createDirectories(logDir);

            if (!Files.exists(logFile))
                Files.createFile(logFile);

        } catch (IOException e) {

        }
    }

    public Path getServerDir() {
        return serverDir;
    }

    public Path getImagesDir() {
        return imagesDir;
    }

    public Path getVideosDir() {
        return videosDir;
    }

    public Path getLogDir() {
        return logDir;
    }

    public Path getLogFile() {
        return logFile;
    }

    public Path getCertsDir() {
        return certsDir;
    }

}
