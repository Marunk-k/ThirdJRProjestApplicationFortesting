package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.History;
import entity.Test;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TestDao {

    private final ObjectMapper objectMapper;
    private final File testsDirectory;

    @SneakyThrows
    public List<Test> findAll() {
        ensureDirectoryExists();
        List<Test> tests = new ArrayList<>();

        try (Stream<Path> paths = Files.list(testsDirectory.toPath())) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith("test_"))
                    .filter(path -> path.toString().toLowerCase().endsWith(".json"))
                    .forEach(path -> {
                        try {
                            Test test = objectMapper.readValue(path.toFile(), Test.class);
                            tests.add(test);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
        return tests;
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

    @SneakyThrows
    private void ensureDirectoryExists() {
        if (!testsDirectory.exists()) {
            Files.createDirectories(testsDirectory.toPath());
        }
    }

    @SneakyThrows
    public void save(Test test) {
        ensureDirectoryExists();
        File testFile = getTestFile(test.getId());
        objectMapper.writeValue(testFile, test);
    }

    private File getTestFile(UUID testId) {
        return new File(testsDirectory, "test_" + testId + ".json");
    }

    @SneakyThrows
    public void deleteTest(UUID testId) {
        ensureDirectoryExists();
        File testFile = getTestFile(testId);

        if (testFile.exists()) {
            Files.delete(testFile.toPath());
        }
    }

    @SneakyThrows
    public void update(Test newTest, UUID testId) {
        ensureDirectoryExists();

        if (findById(testId) != null) {
            newTest.setId(testId);
            save(newTest);
        }
    }
}
