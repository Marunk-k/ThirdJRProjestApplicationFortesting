package util;

import entity.Credentials;
import jakarta.servlet.http.HttpServletRequest;

public class CredentialsExtractor {
    public Credentials extract(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        return Credentials.builder()
                .login(login)
                .password(password)
                .build();
    }
}
