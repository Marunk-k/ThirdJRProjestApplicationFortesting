package servlet.auth;

import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/login",
        initParams = {@WebInitParam(name = "resourceName", value = "/login.jsp")})

public class LoginServlet extends AuthServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        authService.authenticate(req, resp);

        if (authService.isAuthenticate(req)) {
            if (authService.isAdmin(req)) {
                resp.sendRedirect("/secure/admin/adminMain");
            } else {
                resp.sendRedirect("/secure/userMain");
            }
        } else {
            resp.sendRedirect("login.jsp");
        }
    }
}
