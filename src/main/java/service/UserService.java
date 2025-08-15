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
        Optional<User> userOpt = findAll().stream()
                .filter(user -> user.getLogin().equals(credentials.getLogin()))
                .findFirst();

        if (userOpt.isPresent()) {
            User userFromDb = userOpt.get();
            String enteredPassword = credentials.getPassword();
            String storedHash = userFromDb.getPassword();

            if (BCrypt.checkpw(enteredPassword, storedHash)) {
                return userOpt;
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }


    public boolean isExist(String login) {
        List<User> users = findAll();

        return users.stream()
                .anyMatch(user1 -> user1.getLogin().equals(login));
    }
}
