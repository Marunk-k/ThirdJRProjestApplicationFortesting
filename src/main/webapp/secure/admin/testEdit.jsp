<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="entity.Test" %>
<%@ page import="entity.User" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование теста</title>
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

        .question-block {
            background: rgba(255, 255, 255, 0.08);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 25px;
            transition: all 0.3s ease;
        }

        .question-block:hover {
            background: rgba(255, 255, 255, 0.12);
            transform: translateY(-2px);
        }

        .question-text {
            font-size: 20px;
            margin-bottom: 15px;
            color: #ffcc80;
        }

        .answers {
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
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .answer-item:hover {
            background: rgba(255, 255, 255, 0.1);
        }

        .answer-input {
            width: 18px;
            height: 18px;
            cursor: pointer;
        }

        .text-input {
            flex: 1;
            padding: 8px 12px;
            background: rgba(0, 0, 0, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 6px;
            color: white;
            font-size: 16px;
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

        .btn-tests {
            background: linear-gradient(135deg, #9c27b0, #673ab7);
        }

        .btn-tests:hover {
            background: linear-gradient(135deg, #ab47bc, #7e57c2);
            transform: translateY(-3px);
        }

        .question-type {
            margin: 10px 0;
            display: flex;
            gap: 15px;
            align-items: center;
        }

        .type-label {
            font-size: 16px;
            color: #bb86fc;
        }

        .type-select {
            padding: 8px 12px;
            background: rgba(0, 0, 0, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 6px;
            color: white;
            font-size: 16px;
        }

        .add-answer-btn {
            background: rgba(255, 255, 255, 0.1);
            border: none;
            color: #4fc3f7;
            padding: 8px 15px;
            border-radius: 6px;
            cursor: pointer;
            margin-top: 10px;
            transition: background 0.3s;
        }

        .add-answer-btn:hover {
            background: rgba(255, 255, 255, 0.2);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="test-header">
        <h1 class="test-title">Редактирование теста: ${test.name}</h1>
        <div class="test-topic">Тема: ${test.topic}</div>
    </div>

    <form id="editForm" action="${pageContext.request.contextPath}/secure/admin/testEdit" method="post">
        <input type="hidden" name="testId" value="${test.id}">
        <input type="hidden" name="_method" value="put">
        <input type="hidden" name="testId" value="${test.id}">
        <div class="question-block">
            <label class="question-text">Название теста:</label>
            <input type="text" class="text-input" name="testName" value="${test.name}" required>

            <label class="question-text" style="margin-top: 15px; display: block;">Тема теста:</label>
            <input type="text" class="text-input" name="testTopic" value="${test.topic}" required>
        </div>

        <c:forEach var="q" items="${test.questions}" varStatus="status">
            <div class="question-block">
                <label class="question-text">Вопрос ${status.index + 1}:</label>
                <input type="text" class="text-input" name="q${status.index}_description"
                       value="${q.description}" placeholder="Текст вопроса" required>

                <div class="question-type">
                    <span class="type-label">Тип вопроса:</span>
                    <select class="type-select" name="q${status.index}_type">
                        <option value="SINGLE" ${q.type == 'SINGLE' ? 'selected' : ''}>Один правильный ответ</option>
                        <option value="MULTIPLE" ${q.type == 'MULTIPLE' ? 'selected' : ''}>Несколько правильных ответов</option>
                    </select>
                </div>

                <div class="answers">
                    <c:forEach var="answer" items="${q.answers}" varStatus="aStatus">
                        <div class="answer-item">
                            <input type="checkbox" class="answer-input"
                                   name="q${status.index}_correct${aStatus.index}"
                                ${q.rightAnswerIndexes.contains(aStatus.index) ? 'checked' : ''}>

                            <input type="text" class="text-input"
                                   name="q${status.index}_answer${aStatus.index}"
                                   value="${answer}" placeholder="Вариант ответа" required>
                        </div>
                    </c:forEach>

                    <button type="button" class="add-answer-btn"
                            onclick="addAnswer(${status.index})">+ Добавить вариант</button>
                </div>
            </div>
        </c:forEach>

        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/secure/tests">
                <button class="btn-tests" type="button">К списку тестов</button>
            </a>
            <button type="submit" class="btn-submit">Сохранить тест</button>
        </div>
    </form>
</div>

<script>
    function addAnswer(questionIndex) {
        const answersContainer = document.querySelector(`.question-block:nth-child(${questionIndex + 2}) .answers`);
        const answerCount = answersContainer.querySelectorAll('.answer-item').length;

        const newAnswer = document.createElement('div');
        newAnswer.className = 'answer-item';
        newAnswer.innerHTML = `
            <input type="checkbox" class="answer-input"
                   name="q${questionIndex}_correct${answerCount}">

            <input type="text" class="text-input"
                   name="q${questionIndex}_answer${answerCount}"
                   placeholder="Вариант ответа" required>
        `;

        answersContainer.insertBefore(newAnswer, answersContainer.lastChild);
    }

    document.querySelectorAll('.answer-input[type="checkbox"]').forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const item = this.closest('.answer-item');
            if (this.checked) {
                item.style.background = 'rgba(76, 175, 80, 0.2)';
            } else {
                item.style.background = 'rgba(255, 255, 255, 0.05)';
            }
        });

        if (checkbox.checked) {
            checkbox.closest('.answer-item').style.background = 'rgba(76, 175, 80, 0.2)';
        }
    });
</script>

</body>
</html>