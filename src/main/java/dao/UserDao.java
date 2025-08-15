package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserDao {

    private final ObjectMapper objectMapper;
    private final File userFile;

    @SneakyThrows
    public void save(User user) {
        List<User> users = findAll();
        users.add(user);
        objectMapper.writeValue(userFile, users);
    }

    @SneakyThrows
    public List<User> findAll() {
        if (userFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(userFile, new TypeReference<>() {
        });
    }
}
