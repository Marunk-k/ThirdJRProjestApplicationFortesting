package service;

import dao.HistoryDao;
import entity.History;
import entity.TestResult;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HistoryService {
    private final HistoryDao historyDao;

    public List<History> findByUserId(UUID id) {
        return historyDao.findByUserId(id);
    }

    public List<TestResult> extractBestResults(UUID id) {
        List<History> history = historyDao.findByUserId(id);

        return history.stream()
                .map(History::getTestResult)
                .collect(Collectors.toMap(
                        TestResult::getTestId,
                        h -> h,
                        (h1, h2) -> h1.getScore() >= h2.getScore() ? h1 : h2
                ))
                .values().stream().toList();
    }

    public void saveResult(TestResult result, HttpServletRequest req) {
        UUID userId = ((User)req.getSession().getAttribute("user")).getId();

        History history = History.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .testResult(result)
                .completionDate(LocalDateTime.now())
                .build();

        historyDao.save(history);
    }
}
