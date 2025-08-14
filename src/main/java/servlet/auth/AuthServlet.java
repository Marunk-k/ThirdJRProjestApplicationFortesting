package servlet.auth;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthService;
import service.UserService;
import util.CredentialsExtractor;

import java.io.IOException;

@WebServlet(urlPatterns = "/auth")
public class AuthServlet extends HttpServlet {

    UserService userService;
    CredentialsExtractor credentialsExtractor;
    AuthService authService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) config.getServletContext().getAttribute("userService");
        credentialsExtractor = (CredentialsExtractor) config.getServletContext().getAttribute("credentialsExtractor");
        authService = (AuthService) config.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(getInitParameter("resourceName")).forward(req, resp);
    }
}
