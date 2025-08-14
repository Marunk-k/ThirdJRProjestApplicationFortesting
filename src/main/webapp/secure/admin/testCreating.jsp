<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Создать тест</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364);
            background-size: 300% 300%;
            animation: gradient 12s ease infinite;
            margin: 0; padding: 0; color: white;
        }
        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        .container {
            max-width: 900px;
            margin: 50px auto;
            background: rgba(0,0,0,0.4);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
        }
        h2 { text-align: center; margin-bottom: 25px; }
        input, textarea, select { width: 100%; margin-bottom: 10px; padding: 8px; border-radius: 5px; border: none; }
        button {
            padding: 6px 12px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            color: white;
            transition: all 0.3s ease;
            margin-right: 5px;
        }
        .btn-add { background: #2196F3; }
        .btn-remove { background: #F44336; }
        .btn-submit { background: #4CAF50; }
        button:hover { transform: scale(1.05); }
        .question { margin-bottom: 20px; padding: 15px; border: 1px solid rgba(255,255,255,0.3); border-radius: 10px; position: relative; }
        .question h4 { margin-top: 0; }
        .answers input { margin-bottom: 5px; }
        .actions { margin-bottom: 15px; }
    </style>
    <script>
        let questionCount = 0;

        function addQuestion() {
            questionCount++;
            const container = document.getElementById("questionsContainer");

            const div = document.createElement("div");
            div.className = "question";
            div.id = "question-" + questionCount;

            div.innerHTML = `
                <h4>Вопрос ${questionCount}</h4>
                <textarea name="questions[${questionCount}].description" placeholder="Текст вопроса" required></textarea>
                <div class="answers" id="answers-${questionCount}">
                    <input type="text" name="questions[${questionCount}].answers[0]" placeholder="Ответ 1" required>
                    <input type="text" name="questions[${questionCount}].answers[1]" placeholder="Ответ 2" required>
                </div>
                <div class="actions">
                    <button type="button" onclick="addAnswer(${questionCount})" class="btn-add">Добавить ответ</button>
                    <button type="button" onclick="removeQuestion(${questionCount})" class="btn-remove">Удалить вопрос</button>
                </div>
                <label>Правильный ответ:
                    <select name="questions[${questionCount}].rightAnswerIndex" id="rightAnswer-${questionCount}" required>
                        <option value="0">Ответ 1</option>
                        <option value="1">Ответ 2</option>
                    </select>
                </label>
            `;
            container.appendChild(div);
        }

        function addAnswer(questionId) {
            const answersDiv = document.getElementById("answers-" + questionId);
            const rightSelect = document.getElementById("rightAnswer-" + questionId);
            const index = answersDiv.children.length;

            const input = document.createElement("input");
            input.type = "text";
            input.name = `questions[${questionId}].answers[${index}]`;
            input.placeholder = `Ответ ${index + 1}`;
            input.required = true;
            answersDiv.appendChild(input);

            const option = document.createElement("option");
            option.value = index;
            option.text = `Ответ ${index + 1}`;
            rightSelect.appendChild(option);
        }

        function removeQuestion(questionId) {
            const div = document.getElementById("question-" + questionId);
            div.remove();
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Создать новый тест</h2>
    <form action="<%= request.getContextPath() %>/admin/createTest" method="post">
        <label>Название теста:</label>
        <input type="text" name="name" placeholder="Название" required>

        <label>Тема теста:</label>
        <input type="text" name="topic" placeholder="Тема" required>

        <div id="questionsContainer"></div>

        <button type="button" onclick="addQuestion()" class="btn-add">Добавить вопрос</button>
        <button type="submit" class="btn-submit">Сохранить тест</button>
    </form>
</div>
</body>
</html>
