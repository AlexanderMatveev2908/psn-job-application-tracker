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
            String rawVal = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";

            nestKeyVal(dict, rawKey, rawVal);
        }

        return dict;
    }

    private static void nestKeyVal(Map<String, Object> dict, String key, Object val) {
        String[] parts = key.replace("]", "").split("\\[");

        Map<String, Object> curr = dict;
        // ? stop at -2 for lists and -1 for dicts
        int stop = parts[parts.length - 1].isEmpty() ? parts.length - 2 : parts.length - 1;

        for (int i = 0; i < stop; i++) {
            String p = parts[i];

            Map<String, Object> next = (Map<String, Object>) curr.computeIfAbsent(p,
                    k -> new HashMap<String, Object>());
            curr = next;
        }

        String lastKey = parts[parts.length - 1];

        if (lastKey.isEmpty()) {
            String arrListKey = parts[parts.length - 2];
            Object existingVal = curr.get(arrListKey);

            if (existingVal instanceof List) {
                // ? append as normal list
                ((List<Object>) existingVal).add(val);
            } else if (existingVal != null) {
                // ? existing val for curr key but is not a list, shape must be changed from
                // ? prev val(str, int ,etc...) to list.
                List<Object> newArrList = new ArrayList<>();
                newArrList.add(existingVal);
                newArrList.add(val);
                curr.put(arrListKey, newArrList);
            } else {
                // ? non-existent val, add curr val while creating list
                curr.put(arrListKey, new ArrayList<>(List.of(val)));
            }
        } else if (curr.containsKey(lastKey)) {
            Object existingVal = curr.get(lastKey);

            // ? check val being list in case client does not send keys
            // ? with [] at the end for lists
            if (existingVal instanceof List) {
                ((List<Object>) existingVal).add(val);
            } else {
                curr.put(lastKey, new ArrayList<>(List.of(existingVal, val)));
            }
        } else {
            curr.put(lastKey, val);
        }
    }
}