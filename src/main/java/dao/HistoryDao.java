package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.History;
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
        return objectMapper.readValue(historyFile, new TypeReference<>() {
        });
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
