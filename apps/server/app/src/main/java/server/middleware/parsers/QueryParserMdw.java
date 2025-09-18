package server.middleware.parsers;

import java.io.IOException;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings("UseSpecificCatch")
@Component
@Order(0)
public class QueryParserMdw implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest reqHttp = (HttpServletRequest) req;

        Map<String, Object> parsedQuery = ParserManager.nestDict(reqHttp.getQueryString());

        reqHttp.setAttribute("parsedQuery", parsedQuery);

        chain.doFilter(req, res);
    }
}
