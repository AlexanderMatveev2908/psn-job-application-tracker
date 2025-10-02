package server.middleware.parsers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParserManager {

    public static Map<String, Object> nestDict(String query) {
        if (query == null || query.isBlank())
            return null;

        Map<String, Object> dict = new HashMap<>();

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);

            if (kv.length < 2)
                continue;

            String rawKey = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String rawVal = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);

            nestKeyVal(dict, rawKey, rawVal);
        }

        return dict;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static void nestKeyVal(Map<String, Object> dict, String key, Object val) {
        boolean isArrayKey = key.endsWith("[]");
        String[] parts = key.replace("]", "").split("\\[");

        Map<String, Object> curr = dict;
        int lastIdx = isArrayKey ? parts.length - 2 : parts.length - 1;
        if (lastIdx < 0)
            lastIdx = 0;

        for (int i = 0; i < lastIdx; i++) {
            String p = parts[i];
            Map<String, Object> next = (Map<String, Object>) curr.computeIfAbsent(p, k -> new HashMap<>());
            curr = next;
        }

        String lastKey = parts[lastIdx];

        addVal(curr, lastKey, isArrayKey, val);
    }

    private static void addVal(Map<String, Object> curr, String lastKey, boolean isArrayKey, Object val) {
        Object existingVal = curr.get(lastKey);

        if (existingVal instanceof List)
            ((List<Object>) existingVal).add(val);
        else if (existingVal != null)
            curr.put(lastKey, new ArrayList<>(List.of(existingVal, val)));
        else
            curr.put(lastKey, isArrayKey ? new ArrayList<>(List.of(val)) : val);

    }
}