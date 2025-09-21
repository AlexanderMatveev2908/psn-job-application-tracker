package server.middleware.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.lib.etc.Kit;

@Component
@Order(0)
public class CorsMdw implements Filter {

    private final Kit kit;

    public CorsMdw(Kit kit) {
        this.kit = kit;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpRes = (HttpServletResponse) res;

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String origin = httpReq.getHeader("Origin");

        String allowed = kit.getEnvKeeper().getFrontUrl();

        if (origin != null && !origin.equals(allowed)) {
            httpRes.setStatus(403);
            httpRes.setContentType("application/json");
            httpRes.setCharacterEncoding("UTF-8");

            String json = kit.getJack()
                    .writeValueAsString(Map.of("msg", String.format("❌ %s not allowed", origin), "status", 403));

            httpRes.getWriter().write(json);
            return;
        }

        String[] headers = {
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization"
        };

        String allowHeaders = String.join(", ", headers) + ", " +
                String.join(", ", Arrays.stream(headers)
                        .map(String::toLowerCase)
                        .toArray(String[]::new));

        httpRes.setHeader("Access-Control-Allow-Headers", allowHeaders);
        httpRes.setHeader("Access-Control-Allow-Origin", allowed);
        httpRes.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpRes.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            httpRes.setStatus(200);
            return;
        }

        chain.doFilter(req, httpRes);
    }
}
