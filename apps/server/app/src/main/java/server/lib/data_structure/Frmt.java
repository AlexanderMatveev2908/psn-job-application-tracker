package server.lib.data_structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Frmt {

    private final static ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @SuppressWarnings({ "unused", "UseSpecificCatch" })
    public static String toJson(Object obj) {
        try {

            return jack.writeValueAsString(obj);

        } catch (Exception err) {

            System.out.println("❌ err parsing arg to json");

            return null;

        }
    }
}
