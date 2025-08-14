package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Test;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestDao {

    private final ObjectMapper objectMapper;
    private final File testFile;

    @SneakyThrows
    public List<Test> findAll() {
        if (testFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(testFile, new TypeReference<List<Test>>() {});
    }

    public List<Test> findByTopic(String topic) {
        return findAll().stream()
                .filter(test -> test.getTopic().equals(topic))
                .collect(Collectors.toList());
    }

    public Test findById(UUID testId) {
        return findAll().stream()
                .filter(test -> test.getId().equals(testId))
                .findAny()
                .orElse(null);
    }
}
