package service;

import dao.TestDao;
import entity.Test;
import entity.TestQuestion;
import entity.TestResult;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import util.QuestionType;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestService {

    private final TestDao testDao;

    public void prepareTests(HttpServletRequest req) {
        String topicFilter = req.getParameter("topicFilter");
        req.setAttribute("currentFilter", topicFilter);

        List<Test> tests = getFilteredTests(topicFilter);
        req.setAttribute("tests", tests);

        Set<String> uniqueTopics = getAllUniqueTopics();
        req.setAttribute("uniqueTopics", uniqueTopics);
    }

    private List<Test> getFilteredTests(String topicFilter) {
        if (topicFilter != null && !topicFilter.isEmpty()) {
            return testDao.findByTopic(topicFilter);
        }
        return testDao.findAll();
    }

    private Set<String> getAllUniqueTopics() {
        return testDao.findAll().stream()
                .map(Test::getTopic)
                .collect(Collectors.toSet());
    }

    public void prepareTest(HttpServletRequest req) {
        UUID testId = UUID.fromString(req.getParameter("testId"));
        Test test = testDao.findById(testId);
        req.setAttribute("test", test);
    }

    public TestResult extractResult(HttpServletRequest req) {
        UUID testId = UUID.fromString(req.getParameter("testId"));
        Test test = testDao.findById(testId);
        Map<Integer, List<Integer>> userAnswers = extractUserAnswers(req, test);
        return evaluateTest(test, userAnswers);
    }

    private Map<Integer, List<Integer>> extractUserAnswers(HttpServletRequest req, Test test) {
        Map<Integer, List<Integer>> userAnswers = new HashMap<>();

        for (int i = 0; i < test.getQuestions().size(); i++) {
            TestQuestion question = test.getQuestions().get(i);
            List<Integer> answerIndexes = extractAnswerForQuestion(req, i, question);

            if (!answerIndexes.isEmpty()) {
                userAnswers.put(i, answerIndexes);
            }
        }

        return userAnswers;
    }

    private List<Integer> extractAnswerForQuestion(HttpServletRequest req, int questionIndex, TestQuestion question) {
        String paramName = "question_" + questionIndex;
        String[] values;

        if (question.getType() == QuestionType.MULTIPLE) {
            values = req.getParameterValues(paramName + "[]");
        } else {
            String singleVal = req.getParameter(paramName);
            values = singleVal != null ? new String[]{singleVal} : null;
        }

        if (values == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(values)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private TestResult evaluateTest(Test test, Map<Integer, List<Integer>> userAnswers) {
        int totalQuestions = test.getQuestions().size();
        int correctAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            TestQuestion question = test.getQuestions().get(i);
            List<Integer> correctIndexes = question.getRightAnswerIndexes();
            List<Integer> givenIndexes = userAnswers.getOrDefault(i, Collections.emptyList());

            if (isAnswerCorrect(question, correctIndexes, givenIndexes)) {
                correctAnswers++;
            }
        }

        return buildTestResult(test, totalQuestions, correctAnswers);
    }

    private boolean isAnswerCorrect(TestQuestion question,
                                    List<Integer> correctIndexes,
                                    List<Integer> givenIndexes) {
        if (question.getType() == QuestionType.SINGLE) {
            return givenIndexes.size() == 1 && correctIndexes.contains(givenIndexes.get(0));
        } else {
            return new HashSet<>(givenIndexes).equals(new HashSet<>(correctIndexes));
        }
    }

    private TestResult buildTestResult(Test test, int totalQuestions, int correctAnswers) {
        int score = calculateScore(totalQuestions, correctAnswers);

        return TestResult.builder()
                .testId(test.getId())
                .testName(test.getName())
                .testTopic(test.getTopic())
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .score(score)
                .build();
    }

    private int calculateScore(int totalQuestions, int correctAnswers) {
        return (int) Math.round((double) correctAnswers / totalQuestions * 100);
    }

    public void saveNewTest(HttpServletRequest req) {
        String testName = req.getParameter("name");
        String topic = req.getParameter("topic");
        int timeLimitMinutes = extractTimeLimit(req);
        List<TestQuestion> questions = extractQuestionsFromRequest(req);
        User user = getCurrentUser(req);

        Test test = new Test(UUID.randomUUID(), user.getId(), testName, topic, questions, timeLimitMinutes);
        testDao.save(test);
    }

    private int extractTimeLimit(HttpServletRequest req) {
        String timeLimitParam = req.getParameter("timeLimitMinutes");
        if (timeLimitParam != null && !timeLimitParam.isEmpty()) {
            return Integer.parseInt(timeLimitParam);
        }

        return 5;
    }

    private List<TestQuestion> extractQuestionsFromRequest(HttpServletRequest req) {
        List<TestQuestion> questions = new ArrayList<>();
        int i = 0;

        while (true) {
            String desc = req.getParameter("questions[" + i + "].description");
            if (desc == null || desc.trim().isEmpty()) break;

            TestQuestion question = createQuestion(req, i);
            questions.add(question);
            i++;
        }

        return questions;
    }

    private TestQuestion createQuestion(HttpServletRequest req, int questionIndex) {
        String desc = req.getParameter("questions[" + questionIndex + "].description");
        QuestionType type = extractQuestionType(req, questionIndex);
        List<String> answers = extractAnswers(req, questionIndex);
        List<Integer> rightIndexes = extractCorrectAnswers(req, questionIndex, answers.size(), type);

        return new TestQuestion(
                UUID.randomUUID(),
                desc,
                answers,
                rightIndexes,
                type
        );
    }

    private QuestionType extractQuestionType(HttpServletRequest req, int questionIndex) {
        String typeStr = req.getParameter("questions[" + questionIndex + "].type");
        if (typeStr == null) {
            return QuestionType.SINGLE;
        }
        return QuestionType.valueOf(typeStr);
    }

    private List<String> extractAnswers(HttpServletRequest req, int questionIndex) {
        List<String> answers = new ArrayList<>();
        int j = 0;

        while (true) {
            String answerText = req.getParameter("questions[" + questionIndex + "].answers[" + j + "]");
            if (answerText == null) {
                break;
            }
            answers.add(answerText);
            j++;
        }

        return answers;
    }

    private List<Integer> extractCorrectAnswers(HttpServletRequest req,
                                                int questionIndex,
                                                int answersCount,
                                                QuestionType type) {
        List<Integer> rightIndexes = new ArrayList<>();
        String[] corrects = req.getParameterValues("questions[" + questionIndex + "].correct");

        if (corrects != null) {
            for (String c : corrects) {
                int index = Integer.parseInt(c);
                if (index >= 0 && index < answersCount) {
                    rightIndexes.add(index);
                }
            }
        }

        if (type == QuestionType.SINGLE && !rightIndexes.isEmpty()) {
            return List.of(rightIndexes.get(0));
        }

        return rightIndexes;
    }

    private User getCurrentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    public void deleteTest(HttpServletRequest req) {
        UUID testId = UUID.fromString(req.getParameter("testId"));
        testDao.deleteTest(testId);
    }

    public void updateTest(HttpServletRequest req) {
        UUID testId = UUID.fromString(req.getParameter("testId"));
        String name = req.getParameter("testName");
        String topic = req.getParameter("testTopic");
        int timeLimitMinutes = extractTimeLimit(req);
        List<TestQuestion> questions = extractUpdatedQuestions(req);
        Test originalTest = testDao.findById(testId);

        Test test = buildUpdatedTest(testId, name, topic, timeLimitMinutes, questions, originalTest);
        testDao.update(test, testId);
    }

    private List<TestQuestion> extractUpdatedQuestions(HttpServletRequest req) {
        List<TestQuestion> questions = new ArrayList<>();
        int qIndex = 0;

        while (true) {
            String questionParam = "q" + qIndex + "_description";
            String questionText = req.getParameter(questionParam);

            if (questionText == null) {
                break;
            }

            TestQuestion question = createUpdatedQuestion(req, qIndex);
            questions.add(question);
            qIndex++;
        }

        return questions;
    }

    private TestQuestion createUpdatedQuestion(HttpServletRequest req, int questionIndex) {
        String questionText = req.getParameter("q" + questionIndex + "_description");
        QuestionType type = QuestionType.valueOf(req.getParameter("q" + questionIndex + "_type"));
        List<String> answers = new ArrayList<>();
        List<Integer> rightAnswerIndexes = new ArrayList<>();
        int aIndex = 0;

        while (true) {
            String answerParam = "q" + questionIndex + "_answer" + aIndex;
            String answerText = req.getParameter(answerParam);
            if (answerText == null) break;

            answers.add(answerText);

            String correctParam = "q" + questionIndex + "_correct" + aIndex;
            if (isAnswerCorrect(req, correctParam)) {
                rightAnswerIndexes.add(aIndex);
            }
            aIndex++;
        }

        return TestQuestion.builder()
                .id(UUID.randomUUID())
                .description(questionText)
                .answers(answers)
                .rightAnswerIndexes(rightAnswerIndexes)
                .type(type)
                .build();
    }

    private boolean isAnswerCorrect(HttpServletRequest req, String correctParam) {
        String correctValue = req.getParameter(correctParam);
        return correctValue != null && correctValue.equals("on");
    }

    private Test buildUpdatedTest(UUID testId,
                                  String name,
                                  String topic,
                                  int timeLimitMinutes,
                                  List<TestQuestion> questions,
                                  Test originalTest) {
        return Test.builder()
                .id(testId)
                .createdBy(originalTest.getCreatedBy())
                .name(name)
                .topic(topic)
                .questions(questions)
                .timeLimitMinutes(timeLimitMinutes)
                .build();
    }
}