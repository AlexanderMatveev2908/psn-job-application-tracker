package server.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JackMng {
    @Bean
    public ObjectMapper getJack() {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }
}
