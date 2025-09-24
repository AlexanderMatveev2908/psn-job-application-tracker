package server.conf.env_conf;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import io.github.cdimascio.dotenv.Dotenv;
import server.decorators.flow.ErrAPI;
import server.lib.paths.Hiker;

public class EnvLoader implements EnvironmentPostProcessor {

    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        try {
            Path serverDir = Hiker.getServerDir();

            Dotenv dotenv = Dotenv.configure()
                    .directory(serverDir.toString())
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            Properties props = new Properties();
            dotenv.entries().forEach(entr -> props.put(entr.getKey(), entr.getValue()));

            String dbUrl = dotenv.get("DB_URL");
            String dbUs = dotenv.get("DB_US");
            String dbPwd = dotenv.get("DB_PWD");
            String supabaseCa = dotenv.get("SUPABASE_CA");

            if (Stream.of(dbUrl, dbUs, dbPwd).anyMatch(Objects::isNull)) {
                var missing = Stream.of(
                        Map.entry("DB_URL", dbUrl),
                        Map.entry("DB_US", dbUs),
                        Map.entry("DB_PWD", dbPwd),
                        Map.entry("SUPABASE_CA", supabaseCa))
                        .filter(el -> el.getValue() == null || el.getValue().isBlank())
                        .findFirst();

                missing.ifPresent(el -> {
                    throw new ErrAPI(
                            String.format("⚠️ %s key db cnt => %s", el.getValue() == null ? "missing" : "blank",
                                    el.getKey()),
                            500);
                });

            }

            Path certFile = Hiker.getCaFile();
            Files.write(certFile, HexFormat.of().parseHex(supabaseCa));
            Path cartPath = certFile.toAbsolutePath();

            dbUrl = dbUrl + "?sslmode=verify-full&sslrootcert=" + cartPath;

            props.put("DB_URL", dbUrl);

            URI uri = URI.create(dbUrl.replace("jdbc:", ""));

            String host = uri.getHost();
            int port = uri.getPort();
            String dbName = uri.getPath().replaceFirst("/", "");

            StringBuilder r2dbcUrl = new StringBuilder();
            r2dbcUrl.append("r2dbc:postgresql://")
                    .append(dbUs).append(":").append(dbPwd).append("@")
                    .append(host).append(":").append(port).append("/")
                    .append(dbName);
            r2dbcUrl.append("?sslMode=verify-full&sslRootCert=")
                    .append(cartPath);

            props.put("spring.r2dbc.url", r2dbcUrl.toString());
            props.put("spring.r2dbc.username", dbUs);
            props.put("spring.r2dbc.password", dbPwd);

            env.getPropertySources().addFirst(new PropertiesPropertySource("dotenv", props));

        } catch (ErrAPI | IOException err) {
            throw new ErrAPI(err.getMessage(), 500);
        }
    }
}
