package server.middleware.parsers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import server.middleware.dev.ReqAPI;

@SuppressWarnings("UseSpecificCatch")
@Component
@Order(0)
public class FormDataParser implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ReqAPI reqAPI = new ReqAPI((HttpServletRequest) req);
        String contentType = reqAPI.getContentType();

        if (contentType == null || !contentType.startsWith("multipart/form-data")) {
            chain.doFilter(reqAPI, res);
            return;
        }

        String boundary = "--" + contentType.split("boundary=")[1];
        byte[] rawBody = reqAPI.getRawBody();
        String txtBody = new String(rawBody, StandardCharsets.UTF_8);
        String[] parts = txtBody.split(Pattern.quote(boundary));
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            String[] headerAndBody = part.split("\r\n\r\n", 2);

            if (headerAndBody.length < 2)
                continue;

            String headers = headerAndBody[0];
            String value = headerAndBody[1].trim();

            String name;
            Matcher m = Pattern.compile("name=\"([^\"]+)\"").matcher(headers);
            if (!m.find())
                continue;

            name = m.group(1);

            sb.append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            sb.append("&");

        }

        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);

        Map<String, Object> parsedForm = ParserManager.nestDict(sb.toString());
        reqAPI.setAttribute("parsedForm", parsedForm);

        chain.doFilter(reqAPI, res);
    }
}
