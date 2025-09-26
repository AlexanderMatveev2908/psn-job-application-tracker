package server.lib.paths;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import server.decorators.flow.ErrAPI;

public final class Seeker {
    @SuppressWarnings("UseSpecificCatch")
    public static Path grabDir() {

        try {
            Path appDir = Paths.get(
                    Seeker.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI());

            Path serverDir;
            // ? dev case => normal build with classes bytecode
            if (appDir.toString().endsWith("classes/java/main")) {
                int levelsUp = 5;
                String up = (".." + File.separator).repeat(levelsUp);
                serverDir = appDir.resolve(up).normalize();

                return serverDir;
                // ? jar case => spring boot JarLauncher create custom classLoader which no
                // ? more points to oroginal JAR path but to a in-memory structure returning /
            } else if (appDir.toString().equals("/")) {
                appDir = Paths.get(System.getProperty("user.dir"));

                if (!appDir.endsWith("server"))
                    serverDir = appDir.resolve("apps/server").normalize();
                else
                    serverDir = appDir;

                return serverDir;
            } else {
                throw new ErrAPI("not sure where I am 👻", 500);
            }
        } catch (Exception err) {

            throw new ErrAPI(err.getMessage(), 500);
        }

    }
}
