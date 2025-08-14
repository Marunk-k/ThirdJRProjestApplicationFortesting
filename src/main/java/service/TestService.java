package service;

import dao.TestDao;
import dao.UserDao;
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

        Map<Integer, List<Integer>> userAnswers = new HashMap<>();

        for (int i = 0; i < test.getQuestions().size(); i++) {
            TestQuestion question = test.getQuestions().get(i);
            String paramName = "question_" + i;

            String[] values;
            if (question.getType() == QuestionType.MULTIPLE) {
                values = req.getParameterValues(paramName + "[]");
            } else {
                String singleVal = req.getParameter(paramName);
                values = singleVal != null ? new String[]{ singleVal } : null;
            }

            if (values != null) {
                List<Integer> indexes = Arrays.stream(values)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                userAnswers.put(i, indexes);
            }
        }

        return evaluateTest(test, userAnswers);
    }


    private Test getTestById(UUID testId) {
        return testDao.findById(testId);
    }

    private TestResult evaluateTest(Test test, Map<Integer, List<Integer>> userAnswers) {
        int totalQuestions = test.getQuestions().size();
        int correctAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            TestQuestion question = test.getQuestions().get(i);
            List<Integer> correct = question.getRightAnswerIndexes();
            List<Integer> given = userAnswers.getOrDefault(i, Collections.emptyList());

            if (question.getType() == QuestionType.SINGLE) {
                if (given.size() == 1 && correct.contains(given.get(0))) {
                    correctAnswers++;
                }
            } else { // MULTIPLE
                if (new HashSet<>(given).equals(new HashSet<>(correct))) {
                    correctAnswers++;
                }
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


    public void saveNewTest(HttpServletRequest req) {
        String testName = req.getParameter("name");
        String topic = req.getParameter("topic");

        List<TestQuestion> questions = new ArrayList<>();
        int i = 0;

        while (true) {
            String desc = req.getParameter("questions[" + i + "].description");
            if (desc == null || desc.trim().isEmpty()) break;

            String typeStr = req.getParameter("questions[" + i + "].type");
            if (typeStr == null) typeStr = "SINGLE";
            QuestionType type = QuestionType.valueOf(typeStr);

            // Чтение ответов
            List<String> answers = new ArrayList<>();
            int j = 0;
            while (true) {
                String answerText = req.getParameter("questions[" + i + "].answers[" + j + "]");
                if (answerText == null) break;
                answers.add(answerText);
                j++;
            }

            // Чтение правильных ответов - ОСНОВНОЕ ИСПРАВЛЕНИЕ
            List<Integer> rightIndexes = new ArrayList<>();
            String[] corrects = req.getParameterValues("questions[" + i + "].correct");
            if (corrects != null) {
                for (String c : corrects) {
                    try {
                        int index = Integer.parseInt(c);
                        if (index >= 0 && index < answers.size()) {
                            rightIndexes.add(index);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }

            // Для SINGLE вопросов берем только первый выбранный ответ
            if (type == QuestionType.SINGLE && !rightIndexes.isEmpty()) {
                rightIndexes = List.of(rightIndexes.get(0));
            }

            // Создаем вопрос
            TestQuestion q = new TestQuestion(UUID.randomUUID(), desc, answers, rightIndexes, type);
            questions.add(q);
            i++;
        }

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) throw new IllegalStateException("Нет пользователя в сессии");

        Test test = new Test(UUID.randomUUID(), user.getId(), testName, topic, questions);
        testDao.save(test);
    }


    public void deleteTest(UUID testId) {
        testDao.deleteTest(testId);
    }

    public void updateTest(HttpServletRequest req) {
        // 1. Базовые поля теста (с правильными именами)
        String testIdStr = req.getParameter("testId");
        UUID testId = UUID.fromString(testIdStr);
        String name = req.getParameter("testName");      // исправлено
        String topic = req.getParameter("testTopic");    // исправлено

        List<TestQuestion> questions = new ArrayList<>();

        // 2. Перебор вопросов (с правильными именами)
        int qIndex = 0;
        while (true) {
            String questionParam = "q" + qIndex + "_description";  // исправлено
            String questionText = req.getParameter(questionParam);

            // Проверяем существование следующего вопроса
            if (questionText == null) break;

            // 3. Получаем тип вопроса
            String typeParam = "q" + qIndex + "_type";
            String questionTypeStr = req.getParameter(typeParam);
            QuestionType type = QuestionType.valueOf(questionTypeStr); // Должен быть enum

            List<String> answers = new ArrayList<>();
            List<Integer> rightAnswerIndexes = new ArrayList<>();
            int aIndex = 0;

            while (true) {
                String answerParam = "q" + qIndex + "_answer" + aIndex;
                String answerText = req.getParameter(answerParam);
                if (answerText == null) break;

                answers.add(answerText);

                // 4. Проверка чекбоксов (с правильной обработкой)
                String correctParam = "q" + qIndex + "_correct" + aIndex;
                String correctValue = req.getParameter(correctParam);

                if (correctValue != null && correctValue.equals("on")) {
                    rightAnswerIndexes.add(aIndex);
                }
                aIndex++;
            }

            TestQuestion question = TestQuestion.builder()
                    .id(UUID.randomUUID())
                    .description(questionText)
                    .answers(answers)
                    .rightAnswerIndexes(rightAnswerIndexes)
                    .type(type)  // добавлен тип
                    .build();

            questions.add(question);
            qIndex++;
        }

        // 5. Сохранение createdBy из оригинального теста
        Test originalTest = testDao.findById(testId);

        Test test = Test.builder()
                .id(testId)
                .createdBy(originalTest.getCreatedBy()) // важно сохранить создателя
                .name(name)
                .topic(topic)
                .questions(questions)
                .build();

        testDao.update(test, testId);
    }

}
