// package server.conf.db.database;

// import java.net.URI;

// import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
// import io.r2dbc.postgresql.PostgresqlConnectionFactory;
// import io.r2dbc.postgresql.codec.EnumCodec;
// import io.r2dbc.spi.ConnectionFactory;
// import server.models.token.side.AlgT;
// import server.models.token.side.TokenT;

// @Configuration
// public class ConfDB {

// @Bean
// public ConnectionFactory connectionFactory(R2dbcProperties properties) {
// String url = properties.getUrl();
// URI uri = URI.create(url.replace("r2dbc:", ""));

// String host = uri.getHost();
// int port = uri.getPort();
// String dbName = uri.getPath().replaceFirst("/", "");

// return new PostgresqlConnectionFactory(
// PostgresqlConnectionConfiguration.builder()
// .host(host)
// .port(port)
// .username(properties.getUsername())
// .password(properties.getPassword())
// .database(dbName)
// .codecRegistrar(EnumCodec.builder()
// .withEnum("token_t", TokenT.class)
// .withEnum("alg_t", AlgT.class)
// .build())
// .build());
// }
// }
