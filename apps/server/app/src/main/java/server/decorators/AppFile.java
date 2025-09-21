package server.decorators;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class AppFile {
    private final String field;
    private final String filename;
    private final String contentType;
    private final byte[] bts;
    private String filePath;

    public AppFile(
            String field,
            String filename,
            String contentType,
            byte[] bts) {

        this.field = field;
        this.contentType = contentType;
        this.bts = (bts == null) ? new byte[0] : bts.clone();
        this.filePath = null;

        String ext = "";
        int idxDot = filename.lastIndexOf('.');
        if (idxDot != -1 && idxDot < filename.length() - 1) {
            ext = filename.substring(idxDot);
        }

        this.filename = UUID.randomUUID().toString() + ext;
    }

    public Map<String, Object> getFancyShape() {
        Map<String, Object> fancyMap = new LinkedHashMap<>();

        try {
            Class<?> cls = this.getClass();

            for (Field f : cls.getDeclaredFields()) {
                f.setAccessible(true);

                Object val = f.get(this);

                if ("bts".equals(f.getName()))
                    fancyMap.put("bytes", "💾 long binary code...");
                else
                    fancyMap.put(f.getName(), val);

            }
        } catch (IllegalAccessException | IllegalArgumentException err) {
            throw new ErrAPI("err parsing file to fancy shape", 500);
        }

        return fancyMap;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getField() {
        return this.field;
    }

    public byte[] getBts() {
        return bts.clone();
    }

    public void setFilePath(String p) {
        this.filePath = p;
    }

    public String getFilePath() {
        return this.filePath;
    }
}
