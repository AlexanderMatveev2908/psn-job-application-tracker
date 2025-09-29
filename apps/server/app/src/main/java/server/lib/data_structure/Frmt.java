package server.lib.data_structure;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class Frmt {

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

    public static Map<String, Object> toMap(String txt) {

        try {
            return jack.readValue(txt, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception err) {

            System.out.println("❌ err parsing arg to map");

            return null;
        }
    }

    public static String HexToUtf8(String txtHex) {
        byte[] utf8Bytes = HexFormat.of().parseHex(txtHex);
        return new String(utf8Bytes, StandardCharsets.UTF_8);
    }
}
