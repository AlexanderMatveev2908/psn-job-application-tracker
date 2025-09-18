package server.middleware.dev;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CpyReq extends HttpServletRequestWrapper {
    private final byte[] cachedBody;

    public CpyReq(HttpServletRequest req) throws IOException {
        super(req);
        InputStream bodyStream = req.getInputStream();
        this.cachedBody = bodyStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(this.cachedBody);

        return new ServletInputStream() {
            @Override
            public int read() {
                return bodyStream.read();
            }

            @Override
            public boolean isFinished() {
                return bodyStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException("❌ async read not supported");
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(this.cachedBody), StandardCharsets.UTF_8));
    }

    public Map<String, Object> grabBody() throws IOException {
        ObjectMapper jack = new ObjectMapper();

        return jack.readValue(cachedBody, new TypeReference<Map<String, Object>>() {
        });
    }
}
