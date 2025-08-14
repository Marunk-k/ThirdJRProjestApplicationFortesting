package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import util.HttpMethodRequestWrapper;

import java.io.IOException;

@WebFilter(urlPatterns = "/secure/admin/*")
public class MethodFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest httpRequest) {
            String method = httpRequest.getParameter("_method");
            if (method != null && !method.isEmpty()) {
                HttpServletRequest wrapper = new HttpMethodRequestWrapper(httpRequest, method);
                chain.doFilter(wrapper, res);
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
