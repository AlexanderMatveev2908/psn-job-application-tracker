package server.decorators;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import server.decorators.flow.ErrAPI;
import server.lib.paths.Hiker;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@SuppressFBWarnings("EI_EXPOSE_REP")
@Getter
public class AppFile {
    private final String field;
    private final String filename;
    private final String contentType;
    private final byte[] bts;
    private final Path filePath;

    public AppFile(
            String field,
            String filename,
            String contentType,
            byte[] bts) {

        this.field = field;
        this.contentType = contentType;
        this.bts = (bts == null) ? new byte[0] : bts.clone();

        String ext = "";
        int idxDot = filename.lastIndexOf('.');
        if (idxDot != -1 && idxDot < filename.length() - 1) {
            ext = filename.substring(idxDot);
        }

        this.filename = UUID.randomUUID().toString() + ext;
        this.filePath = Hiker.ASSETS_DIR.resolve(this.field).resolve(this.filename);
    }

    public void saveLocally() {
        try {
            Files.write(this.getFilePath(), this.getBts(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException err) {
            throw new ErrAPI("err saving asset locally", 500);
        }
    }

    public void deleteLocally() {
        try {
            Files.deleteIfExists(this.getFilePath());
        } catch (IOException err) {
            throw new ErrAPI("err deleting asset locally", 500);
        }
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

}
