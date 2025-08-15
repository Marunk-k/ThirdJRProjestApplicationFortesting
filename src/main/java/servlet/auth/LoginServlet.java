package servlet.auth;

import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AuthResult;

import java.io.IOException;

@WebServlet(urlPatterns = "/login",
        initParams = {@WebInitParam(name = "resourceName", value = "/login.jsp")})
public class LoginServlet extends AuthServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthResult result = authService.authenticate(req);
        System.out.println(result.getStatus());
        switch (result.getStatus()) {
            case SUCCESS:
                if (authService.isAdmin(req)) {
                    resp.sendRedirect("/secure/admin/adminMain");
                } else {
                    resp.sendRedirect("/secure/userMain");
                }
                break;

            case BLOCKED:
                resp.sendRedirect("/blocked.jsp");
                break;

            case FAILED:
                req.getSession().setAttribute("error", "Неверный логин или пароль");
                resp.sendRedirect("/login.jsp");
                break;
        }
    }
}
