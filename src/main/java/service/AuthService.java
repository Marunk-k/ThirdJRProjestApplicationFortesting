package service;

import entity.Credentials;
import entity.LoginAttempt;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import util.CredentialsExtractor;
import util.Role;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class AuthService {

    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    private final CredentialsExtractor credentialsExtractor;
    private final UserService userService;

    public void authenticate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Credentials credentials = credentialsExtractor.extract(req);

        loginAttempts.putIfAbsent(credentials.getLogin(), new LoginAttempt());
        LoginAttempt loginAttempt = loginAttempts.get(credentials.getLogin());

        if (loginAttempt.isBlockedExpired()) {
            loginAttempt.setCountOfAttempts(new AtomicInteger(0));
        }

        if (loginAttempt.isBlocked()) {
            //req.setAttribute(429);
        }

        Optional<User> optionalUser = userService.findUserByCredentials(credentials);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            req.getSession().setAttribute("user", user);
        } else {
            loginAttempt.incrementCountOfAttempts();
        }
    }

    public boolean register(HttpServletRequest req) throws IOException {
        Credentials credentials = credentialsExtractor.extract(req);
        if (userService.isExist(credentials.getLogin())) {
            return false;
        } else {
            userService.save(credentials);
            return true;
        }
    }

    public boolean isAuthenticate(HttpServletRequest req) {
        User user = (User)req.getSession().getAttribute("user");
        return user != null;
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAdmin(HttpServletRequest req) {
        HttpSession session = ((HttpServletRequest) req).getSession();
        User user = (User) session.getAttribute("user");
        Role role = user.getRole();
        return role.equals(Role.ADMIN);
    }
}
