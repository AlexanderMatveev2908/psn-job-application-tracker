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
import server.decorators.ReqAPI;

@SuppressWarnings("UseSpecificCatch")
@Component
@Order(5)
public class QueryParserMdw implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        ReqAPI reqAPI = (ReqAPI) req;

        Map<String, Object> parsedQuery = ParserManager.nestDict(reqAPI.getQueryString());

        reqAPI.setAttribute("parsedQuery", parsedQuery);

        chain.doFilter(reqAPI, res);
    }
}
