package server.lib.data_structure;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import server.decorators.flow.ErrAPI;

public final class Prs {

    private static final ObjectMapper jack = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT)
            .addModule(new Jdk8Module()).configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false).build();

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

    public static Map<String, Object> hexToMap(String arg) {
        return jsonToMap(hexToUtf8(arg));
    }

    public static byte[] utf8ToBinary(String arg) {
        return arg.getBytes(StandardCharsets.UTF_8);
    }

    public static String binaryToUtf8(byte[] arg) {
        return new String(arg, StandardCharsets.UTF_8);
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

    public static LinkedHashMap<String, Object> linkedMap(Object... kvp) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        if (kvp.length % 2 != 0)
            throw new ErrAPI("passed odd pairs kv", 400);

        for (int i = 0; i < kvp.length; i += 2)
            map.put((String) kvp[i], kvp[i + 1]);

        return map;
    }

    public static String base64ToUtf8(String arg) {
        return binaryToUtf8(Base64.getDecoder().decode(arg));
    }

    public static Map<String, Object> base64ToMap(String arg) {
        String json = base64ToUtf8(arg);
        return jsonToMap(json);
    }

    public static String mapToBase64(Map<String, Object> arg) {
        String json = toJson(arg);
        byte[] binary = utf8ToBinary(json);
        byte[] binaryBase64 = Base64.getEncoder().encode(binary);

        return binaryToUtf8(binaryBase64);

    }
}
