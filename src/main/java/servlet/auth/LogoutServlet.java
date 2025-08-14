package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/secure/logout",
initParams = {@WebInitParam(name = "resourceName", value = "/login.jsp")})
public class LogoutServlet extends AuthServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        authService.logout(req, resp);
        resp.sendRedirect("login.jsp");
    }
}
