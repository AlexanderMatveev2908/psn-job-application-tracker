package server.middleware.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.conf.env.EnvKeeper;

@Component
@Order(0)
public class CorsMdw implements Filter {

    private final EnvKeeper env;
    private final ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public CorsMdw(EnvKeeper env) {
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) res;

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String origin = httpReq.getHeader("Origin");

        String allowed = env.getFrontUrl();

        if (origin != null && !origin.equals(allowed)) {
            httpRes.setStatus(403);
            httpRes.setContentType("application/json");
            httpRes.setCharacterEncoding("UTF-8");

            String json = jack
                    .writeValueAsString(Map.of("msg", String.format("❌ %s not allowed", origin), "status", 403));

            httpRes.getWriter().write(json);
            return;
        }

        httpRes.setHeader("Access-Control-Allow-Origin", allowed);
        httpRes.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpRes.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, httpRes);
    }
}
