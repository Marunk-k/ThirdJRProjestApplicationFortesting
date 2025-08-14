package service;

import dao.TestDao;
import dao.UserDao;
import entity.Test;
import entity.TestQuestion;
import entity.TestResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestService {

    private final TestDao testDao;

    public void prepareTests(HttpServletRequest req, String topicFilter) {
        List<Test> tests;

        if (topicFilter != null && !topicFilter.isEmpty()) {
            tests = testDao.findByTopic(topicFilter);
        } else {
            tests = testDao.findAll();
        }

        req.setAttribute("tests", tests);

        Set<String> uniqueTopics = testDao.findAll().stream()
                .map(Test::getTopic)
                .collect(Collectors.toSet());

        req.setAttribute("uniqueTopics", uniqueTopics);
    }

    public void prepareTestById(HttpServletRequest req, UUID testId) {
        Test test = testDao.findById(testId);
        req.setAttribute("test", test);
    }

    public TestResult extractResult(HttpServletRequest req) {
        UUID testId = UUID.fromString(req.getParameter("testId"));
        Test test = getTestById(testId);

        Map<Integer, Integer> userAnswers = new HashMap<>();

        for (int i = 0; i < test.getQuestions().size(); i++) {
            String paramName = "question_" + i;
            String answerIndex = req.getParameter(paramName);

            if (answerIndex != null) {
                userAnswers.put(i, Integer.parseInt(answerIndex));
            }
        }

        return evaluateTest(test, userAnswers);
    }

    private Test getTestById(UUID testId) {
        return testDao.findById(testId);
    }

    private TestResult evaluateTest(Test test, Map<Integer, Integer> userAnswers) {
        int totalQuestions = test.getQuestions().size();
        int correctAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            TestQuestion question = test.getQuestions().get(i);
            Integer userAnswerIndex = userAnswers.get(i);

            if (userAnswerIndex != null &&
                    userAnswerIndex == question.getRightAnswerIndex()) {
                correctAnswers++;
            }
        }

        return TestResult.builder()
                .testId(test.getId())
                .testName(test.getName())
                .testTopic(test.getTopic())
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .score((int) Math.round((double) correctAnswers / totalQuestions * 100))
                .build();
    }
}
