package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.History;
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
public class HistoryDao {
    private final ObjectMapper objectMapper;
    private final File historyFile;

    @SneakyThrows
    public List<History> findAll() {
        if (historyFile.length() == 0) {
            return new ArrayList<>();
        }
        System.out.println(historyFile.length());
        System.out.println(objectMapper.readValue(historyFile, new TypeReference<List<History>>() {}));
        return objectMapper.readValue(historyFile, new TypeReference<List<History>>() {});
    }

    public List<History> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(history -> history.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void save(History history) {
        List<History> histories = findAll();
        histories.add(history);
        objectMapper.writeValue(historyFile, histories);
    }
}
