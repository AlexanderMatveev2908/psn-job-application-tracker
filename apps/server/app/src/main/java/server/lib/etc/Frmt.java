package server.lib.etc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Frmt {

    private final static ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static String toJson(Object obj) {
        try {
            return jack.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
