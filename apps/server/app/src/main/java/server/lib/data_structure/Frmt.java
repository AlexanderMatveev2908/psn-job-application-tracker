package server.lib.data_structure;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import server.decorators.flow.ErrAPI;

public final class Frmt {

    private final static ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new Jdk8Module());

    @SuppressWarnings({ "unused", "UseSpecificCatch" })
    public static String toJson(Object obj) {
        try {
            return jack.writeValueAsString(obj);
        } catch (Exception err) {
            System.out.println("❌ err parsing arg to json");
            return null;
        }
    }

    public static Map<String, Object> jsonToMap(String txt) {
        try {
            return jack.readValue(txt, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception err) {
            System.out.println("❌ err parsing arg to map");
            return null;
        }
    }

    public static String hexToUtf8(String txtHex) {
        byte[] utf8Bytes = HexFormat.of().parseHex(txtHex);
        return new String(utf8Bytes, StandardCharsets.UTF_8);
    }

    public static String binaryToHex(byte[] arg) {
        return HexFormat.of().formatHex(arg);
    }

    public static byte[] hexToBinary(String arg) {
        return HexFormat.of().parseHex(arg);
    }

    public static byte[] utf8ToBinary(String arg) {
        return arg.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] mapToBinary(Map<String, Object> arg) {
        return utf8ToBinary(toJson(arg));
    }

    public static long fromAnyToLong(Object arg) {
        if (arg == null)
            throw new ErrAPI("expected string or number received null");

        if (arg instanceof Number num)
            return num.longValue();

        if (arg instanceof String str)
            return Long.parseLong(str);

        throw new ErrAPI("unknown arg type");
    }

}
