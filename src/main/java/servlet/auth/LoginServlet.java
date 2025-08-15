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
        handleAuthenticationResult(result, req, resp);
    }

    private void handleAuthenticationResult(AuthResult result, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        switch (result.getStatus()) {
            case SUCCESS:
                handleSuccessfulAuthentication(req, resp);
                break;
            case BLOCKED:
                handleBlockedAuthentication(resp);
                break;
            case FAILED:
                handleFailedAuthentication(req, resp);
                break;
            default:
                handleUnexpectedStatus(resp);
        }
    }

    private void handleSuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String redirectPath = authService.isAdmin(req)
                ? "/secure/admin/adminMain"
                : "/secure/userMain";
        resp.sendRedirect(redirectPath);
    }

    private void handleBlockedAuthentication(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/blocked.jsp");
    }

    private void handleFailedAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.getSession().setAttribute("error", "Неверный логин или пароль");
        resp.sendRedirect("/login.jsp");
    }

    private void handleUnexpectedStatus(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected authentication status");
    }
}
