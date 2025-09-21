package server.decorators.flow;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@SuppressWarnings("UseSpecificCatch")
public class ReqAPI extends HttpServletRequestWrapper {
    private static final ObjectMapper jack = new ObjectMapper();
    private final byte[] cachedBody;

    public ReqAPI(HttpServletRequest req) throws IOException {
        super(req);

        byte[] body;
        try (InputStream bodyStream = req.getInputStream()) {
            body = bodyStream.readAllBytes();
        } catch (IOException e) {
            body = new byte[0];
        }

        this.cachedBody = body;
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

    public <T> T grabBody() {

        try {
            if (cachedBody.length == 0)
                return null;

            return jack.readValue(cachedBody, new TypeReference<T>() {
            });

        } catch (Exception err) {
            return null;
        }
    }

    public byte[] getRawBody() {
        return cachedBody.clone();
    }

}
