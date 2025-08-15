package filter;

import entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import util.Role;

import java.io.IOException;

@WebFilter(urlPatterns = "/secure/admin/*")
public class RoleFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) req).getSession();
        User user = (User) session.getAttribute("user");
        Role role = user.getRole();
        HttpServletRequest request = (HttpServletRequest) req;

        if (role.equals(Role.ADMIN)) {
            chain.doFilter(req, res);
        } else {
            req.getRequestDispatcher("/secure/userMain").forward(request, res);
        }

    }
}
