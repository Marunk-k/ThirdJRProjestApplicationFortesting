<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="entity.Test" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page isELIgnored="false" %>

<%
    Test test = (Test) request.getAttribute("test");
    LocalDateTime endTime = LocalDateTime.now().plusMinutes(test.getTimeLimitMinutes());
    String formattedEndTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Прохождение теста</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364);
            background-size: 300% 300%;
            animation: gradient 12s ease infinite;
            margin: 0;
            padding: 0;
            color: white;
        }

        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .container {
            max-width: 800px;
            margin: 30px auto;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.4);
        }

        .test-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .test-title {
            font-size: 28px;
            margin-bottom: 10px;
            color: #4fc3f7;
        }

        .test-topic {
            font-size: 18px;
            color: #81c784;
        }

        .question-container {
            background: rgba(255, 255, 255, 0.08);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 25px;
            transition: all 0.3s ease;
        }

        .question-container:hover {
            background: rgba(255, 255, 255, 0.12);
            transform: translateY(-2px);
        }

        .question-text {
            font-size: 20px;
            margin-bottom: 15px;
            color: #ffcc80;
        }

        .answers-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .answer-item {
            margin-bottom: 12px;
            padding: 10px 15px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 8px;
            transition: all 0.2s ease;
        }

        .answer-item:hover {
            background: rgba(255, 255, 255, 0.1);
        }

        .answer-label {
            display: flex;
            align-items: center;
            cursor: pointer;
            font-size: 16px;
        }

        .answer-input {
            margin-right: 12px;
            width: 18px;
            height: 18px;
            cursor: pointer;
        }

        .btn-container {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        button {
            padding: 12px 25px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-submit {
            background: linear-gradient(135deg, #4CAF50, #2e7d32);
        }

        .btn-submit:hover {
            background: linear-gradient(135deg, #66bb6a, #388e3c);
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        .btn-submit:disabled {
            background: linear-gradient(135deg, #78909c, #546e7a);
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }

        .required-star {
            color: #ff5252;
            margin-left: 4px;
        }

        .timer-container {
            position: fixed;
            top: 20px;
            right: 20px;
            background: rgba(0, 0, 0, 0.7);
            color: white;
            padding: 15px 20px;
            border-radius: 10px;
            font-size: 24px;
            font-weight: bold;
            z-index: 1000;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .timer-label {
            font-size: 16px;
            margin-bottom: 5px;
            color: #bb86fc;
        }

        .timer-value {
            font-size: 28px;
            color: #4fc3f7;
        }

        .end-time {
            font-size: 14px;
            margin-top: 5px;
            color: #81c784;
        }
    </style>
</head>
<body>

<div class="timer-container">
    <div class="timer-label">Осталось времени:</div>
    <div class="timer-value" id="timer">${test.timeLimitMinutes}:00</div>
    <div class="end-time">Тест завершится в <span id="endTime"><%= formattedEndTime %></span></div>
</div>

<div class="container">
    <div class="test-header">
        <h1 class="test-title">${test.name}</h1>
        <div class="test-topic">Тема: ${test.topic}</div>
    </div>

    <form id="testForm" action="${pageContext.request.contextPath}/secure/testPassing" method="post">
        <input type="hidden" name="testId" value="${test.id}">

        <c:forEach var="question" items="${test.questions}" varStatus="qLoop">
            <div class="question-container">
                <div class="question-text">
                    Вопрос ${qLoop.index + 1}: ${question.description}
                    <span class="required-star">*</span>
                </div>

                <ul class="answers-list">
                    <c:forEach var="answer" items="${question.answers}" varStatus="aLoop">
                        <li class="answer-item">
                            <label class="answer-label">
                                <input
                                        type="${question.type == 'SINGLE' ? 'radio' : 'checkbox'}"
                                        class="answer-input"
                                        name="question_${qLoop.index}${question.type == 'MULTIPLE' ? '[]' : ''}"
                                        value="${aLoop.index}"
                                    ${question.type == 'SINGLE' ? 'required' : ''}
                                >
                                    ${answer}
                            </label>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>

        <div class="btn-container">
            <div class="top-buttons">
                <a href="${pageContext.request.contextPath}/secure/tests">
                    <button class=".btn-back" type="button">К списку тестов</button>
                </a>
            </div>
            <button type="submit" class="btn-submit" id="submitBtn">Проверить результаты</button>
        </div>
    </form>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        try {
            const timeLimitMinutes = ${test.timeLimitMinutes};
            let timeLeft = timeLimitMinutes * 60;
            let timerInterval;

            const timerElement = document.getElementById('timer');
            const testForm = document.getElementById('testForm');
            const submitBtn = document.getElementById('submitBtn');

            if (!timerElement || !testForm) {
                console.error('Critical elements not found!');
                return;
            }

            function updateTimerDisplay() {
                try {
                    if (timeLeft <= 0) {
                        timerElement.textContent = "00:00";
                        timerElement.style.color = "#ff5252";
                        return;
                    }

                    const minutes = Math.floor(timeLeft / 60);
                    const seconds = timeLeft % 60;

                    timerElement.textContent =
                        (minutes < 10 ? '0' + minutes : minutes) + ':' +
                        (seconds < 10 ? '0' + seconds : seconds);

                    if (timeLeft <= 60) {
                        timerElement.style.color = '#ff5252';
                    } else if (timeLeft <= 180) {
                        timerElement.style.color = '#ffcc80';
                    } else {
                        timerElement.style.color = '#4fc3f7';
                    }
                } catch (e) {
                    console.error("Error updating timer display:", e);
                }
            }

            function startTimer() {
                try {
                    updateTimerDisplay();

                    timerInterval = setInterval(function() {
                        timeLeft--;
                        updateTimerDisplay();

                        if (timeLeft <= 0) {
                            clearInterval(timerInterval);
                            timerElement.style.color = "#ff5252";
                            timerElement.textContent = "00:00";

                            alert("Время вышло! Тест будет автоматически отправлен");
                            testForm.submit();
                        }
                    }, 1000);
                } catch (e) {
                    console.error("Timer error:", e);
                }
            }

            startTimer();

            testForm.addEventListener('submit', function(e) {
                clearInterval(timerInterval);

                const allQuestions = document.querySelectorAll('.question-container');
                let allAnswered = true;

                allQuestions.forEach(question => {
                    const inputs = question.querySelectorAll('input[type="radio"], input[type="checkbox"]');
                    const answered = Array.from(inputs).some(input => input.checked);

                    if (!answered) {
                        allAnswered = false;
                        question.style.border = "2px solid #ff5252";
                    } else {
                        question.style.border = "none";
                    }
                });

                if (!allAnswered) {
                    e.preventDefault();
                    alert('Нужно ответить на все вопросы!');
                    startTimer();
                }
            });

            document.querySelectorAll('.answer-input').forEach(input => {
                input.addEventListener('change', function() {
                    const item = this.closest('.answer-item');
                    if (this.checked) {
                        item.style.background = 'rgba(76, 175, 80, 0.2)';
                    } else {
                        item.style.background = 'rgba(255, 255, 255, 0.05)';
                    }
                });

                if (input.checked) {
                    input.closest('.answer-item').style.background = 'rgba(76, 175, 80, 0.2)';
                }
            });

        } catch (e) {
            console.error("Global error in testPassing page:", e);
        }
    });
</script>

</body>
</html>