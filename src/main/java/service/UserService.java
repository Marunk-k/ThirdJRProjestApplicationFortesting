package service;

import dao.UserDao;
import entity.Credentials;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import util.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void save(Credentials credentials) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .role(Role.USER)
                .login(credentials.getLogin())
                .password(credentials.getPassword())
                .build();

        userDao.save(user);
    }

    private List<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findUserByCredentials(Credentials credentials) {
        Optional<User> userOpt = findUserByLogin(credentials.getLogin());

        if (userOpt.isPresent()) {
            if (isPasswordValid(credentials.getPassword(), userOpt.get())) {
                return userOpt;
            }
        }
        return Optional.empty();
    }

    private Optional<User> findUserByLogin(String login) {
        return findAll().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    private boolean isPasswordValid(String password, User userOpt) {
        String storedHash = userOpt.getPassword();
        return BCrypt.checkpw(password, storedHash);
    }


    public boolean isExist(String login) {
        List<User> users = findAll();

        return users.stream()
                .anyMatch(user1 -> user1.getLogin().equals(login));
    }
}
