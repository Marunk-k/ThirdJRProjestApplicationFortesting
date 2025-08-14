<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Результаты теста</title>
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

        .result-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .result-title {
            font-size: 28px;
            margin-bottom: 10px;
            color: #4fc3f7;
        }

        .result-summary {
            background: rgba(255, 255, 255, 0.08);
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
            text-align: center;
        }

        .result-score {
            font-size: 48px;
            font-weight: bold;
            color: #ffd54f;
            margin-bottom: 10px;
        }

        .result-details {
            font-size: 18px;
            margin-bottom: 15px;
        }

        .result-stat {
            display: inline-block;
            margin: 0 15px;
            padding: 8px 15px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 8px;
        }

        .btn-container {
            text-align: center;
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
            margin: 0 10px;
        }

        .btn-menu {
            background: linear-gradient(135deg, #2196F3, #0d47a1);
        }

        .btn-menu:hover {
            background: linear-gradient(135deg, #42a5f5, #1565c0);
            transform: translateY(-3px);
        }

        .btn-tests {
            background: linear-gradient(135deg, #4CAF50, #2e7d32);
        }

        .btn-tests:hover {
            background: linear-gradient(135deg, #66bb6a, #388e3c);
            transform: translateY(-3px);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="result-header">
        <h1 class="result-title">Результаты теста: ${testResult.testName}</h1>
        <div class="result-topic">Тема: ${testResult.testTopic}</div>
    </div>

    <div class="result-summary">
        <div class="result-score">${testResult.score}%</div>
        <div class="result-details">
            <span class="result-stat">Правильно: ${testResult.correctAnswers}</span>
            <span class="result-stat">Всего вопросов: ${testResult.totalQuestions}</span>
        </div>
        <c:choose>
            <c:when test="${testResult.score >= 80}">
                <div class="result-details">Отлично!</div>
            </c:when>
            <c:when test="${testResult.score >= 60}">
                <div class="result-details">Хорошо -_-</div>
            </c:when>
            <c:otherwise>
                <div class="result-details">Пойдёт..</div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="btn-container">
        <a href="${pageContext.request.contextPath}/secure/userMain">
            <button class="btn-menu">В главное меню</button>
        </a>
        <a href="${pageContext.request.contextPath}/secure/tests">
            <button class="btn-tests">К списку тестов</button>
        </a>
    </div>
</div>

</body>
</html>