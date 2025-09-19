package server.decorators;

import java.util.UUID;

public class AppFile {
    private final String field;
    private final String filename;
    private final String contentType;
    private final byte[] bts;

    public AppFile(
            String field,
            String filename,
            String contentType,
            byte[] bts) {

        this.field = field;
        this.contentType = contentType;
        this.bts = bts;

        String ext = "";
        int idxDot = filename.lastIndexOf('.');
        if (idxDot != -1 && idxDot < filename.length() - 1) {
            ext = filename.substring(idxDot);
        }

        this.filename = UUID.randomUUID().toString() + ext;
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
        return this.bts;
    }
}
