<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Создать тест</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364); color: white; margin: 0; padding: 0;}
        .container { max-width: 900px; margin: 50px auto; background: rgba(0,0,0,0.4); padding: 30px; border-radius: 15px; }
        h2 { text-align: center; margin-bottom: 25px; }
        input[type="text"], textarea, select { width: 100%; margin-bottom: 10px; padding: 8px; border-radius: 5px; border: none; box-sizing: border-box;}
        button { padding: 6px 12px; border: none; border-radius: 8px; cursor: pointer; font-size: 14px; color: white; margin-right: 5px;}
        .btn-add { background: #2196F3; }
        .btn-remove { background: #F44336; }
        .btn-submit { background: #4CAF50; }
        .btn-back { background: gray; }
        .answers div { display: flex; align-items: center; gap: 10px; margin-bottom: 5px;}
        .answers input[type="text"] { flex: 1; }
        .answers input[type="checkbox"] { transform: scale(1.3); cursor: pointer; }
        .time-limit {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .time-limit input {
            width: 100px;
        }
    </style>
    <script>
        let questionCount = 0;

        function addQuestion() {
            const container = document.getElementById("questionsContainer");
            const div = document.createElement("div");
            div.className = "question";

            div.innerHTML = `
        <h4>Вопрос</h4>
        <textarea name="" placeholder="Текст вопроса" required></textarea>
        <label>Тип вопроса:
            <select name="" class="question-type" onchange="updateAnswerType(this)">
                <option value="SINGLE">Один правильный</option>
                <option value="MULTIPLE">Несколько правильных</option>
            </select>
        </label>
        <div class="answers">
            <div>
                <input type="text" name="" placeholder="Ответ 1" required>
                <input type="checkbox" name="" value="0">
            </div>
            <div>
                <input type="text" name="" placeholder="Ответ 2" required>
                <input type="checkbox" name="" value="1">
            </div>
        </div>
        <div class="actions">
            <button type="button" onclick="addAnswer(this)" class="btn-add">Добавить ответ</button>
            <button type="button" onclick="removeQuestion(this)" class="btn-remove">Удалить вопрос</button>
        </div>
    `;
            container.appendChild(div);
            renumberQuestions();
        }

        function addAnswer(btn) {
            const answersDiv = btn.parentElement.parentElement.querySelector('.answers');
            const index = answersDiv.children.length;
            const wrapper = document.createElement("div");
            wrapper.innerHTML = `
        <input type="text" name="" placeholder="Ответ ${index + 1}" required>
        <input type="checkbox" name="" value="${index}">
    `;
            answersDiv.appendChild(wrapper);
            renumberQuestions();
        }

        function removeQuestion(btn) {
            btn.closest('.question').remove();
            renumberQuestions();
        }

        function renumberQuestions() {
            const questions = document.querySelectorAll('.question');
            questions.forEach((q, qIdx) => {
                q.querySelector('h4').textContent = `Вопрос ${qIdx + 1}`;
                const textarea = q.querySelector('textarea');
                textarea.name = `questions[${qIdx}].description`;

                const select = q.querySelector('select.question-type');
                select.name = `questions[${qIdx}].type`;

                const answers = q.querySelectorAll('.answers div');
                answers.forEach((ansDiv, aIdx) => {
                    ansDiv.querySelector('input[type="text"]').name = `questions[${qIdx}].answers[${aIdx}]`;

                    const checkbox = ansDiv.querySelector('input[type="checkbox"]');
                    checkbox.name = `questions[${qIdx}].correct`;
                    checkbox.value = aIdx;
                });
            });
        }

        function updateAnswerType(select) {
            const questionDiv = select.closest('.question');
            const answersDiv = questionDiv.querySelector('.answers');
            const isMultiple = select.value === 'MULTIPLE';

            answersDiv.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
                checkbox.name = `questions[${getQuestionIndex(questionDiv)}].correct`;
            });
        }

        function getQuestionIndex(questionDiv) {
            const questions = Array.from(document.querySelectorAll('.question'));
            return questions.indexOf(questionDiv);
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Создать новый тест</h2>
    <form action="<%= request.getContextPath() %>/secure/admin/testCreating" method="post">
        <label>Название теста:</label>
        <input type="text" name="name" placeholder="Название" required>

        <label>Тема теста:</label>
        <input type="text" name="topic" placeholder="Тема" required>

        <div class="time-limit">
            <label>Лимит времени (минут):</label>
            <input type="number" name="timeLimitMinutes" min="1" value="30" required>
        </div>

        <div id="questionsContainer"></div>

        <button type="button" onclick="addQuestion()" class="btn-add">Добавить вопрос</button>
        <button type="submit" class="btn-submit">Сохранить тест</button>
        <button type="button" onclick="history.back()" class="btn-back">Назад</button>
    </form>
</div>
</body>
</html>
