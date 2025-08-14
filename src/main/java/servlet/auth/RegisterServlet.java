package servlet.auth;

import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/register",
            initParams = {@WebInitParam(name = "resourceName", value = "/register.jsp")})

public class RegisterServlet extends AuthServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (authService.register(req)) {
            resp.sendRedirect("/login");
        } else {
            req.setAttribute("registrationError", "Username is not unique");
        }
    }
}
