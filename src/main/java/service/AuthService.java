package service;

import entity.Credentials;
import entity.LoginAttempt;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import util.AuthResult;
import util.CredentialsExtractor;
import util.Role;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class AuthService {

    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    private final CredentialsExtractor credentialsExtractor;
    private final UserService userService;

    public AuthResult authenticate(HttpServletRequest req) {
        Credentials credentials = credentialsExtractor.extract(req);

        loginAttempts.putIfAbsent(credentials.getLogin(), new LoginAttempt());
        LoginAttempt loginAttempt = loginAttempts.get(credentials.getLogin());

        if (loginAttempt.isBlocked()) {
            return new AuthResult(AuthResult.Status.BLOCKED);
        }

        if (loginAttempt.isBlockedExpired()) {
            loginAttempt.reset();
        }

        Optional<User> optionalUser = userService.findUserByCredentials(credentials);

        if (optionalUser.isPresent()) {
            loginAttempt.reset();
            User user = optionalUser.get();
            req.getSession().setAttribute("user", user);
            return new AuthResult(AuthResult.Status.SUCCESS);
        } else {
            loginAttempt.incrementCountOfAttempts();
            return new AuthResult(AuthResult.Status.FAILED);
        }
    }


    public boolean register(HttpServletRequest req) throws IOException {
        Credentials credentials = credentialsExtractor.extract(req);
        String hashedPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
        credentials.setPassword(hashedPassword);
        if (userService.isExist(credentials.getLogin())) {
            return false;
        } else {
            userService.save(credentials);
            return true;
        }
    }

    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Role role = user.getRole();
        return role.equals(Role.ADMIN);
    }
}
