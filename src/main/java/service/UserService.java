package service;

import dao.UserDao;
import entity.Credentials;
import entity.User;
import lombok.RequiredArgsConstructor;
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
        List<User> users = findAll();

        return users.stream()
                .filter(user1 -> user1.getLogin().equals(credentials.getLogin()))
                .findAny();
    }

    public boolean isExist(String login) {
        List<User> users = findAll();

        return users.stream()
                .anyMatch(user1 -> user1.getLogin().equals(login));
    }
}
