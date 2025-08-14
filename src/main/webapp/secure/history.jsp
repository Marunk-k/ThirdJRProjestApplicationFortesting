<%@ page import="entity.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%
    User currentUser = (User) session.getAttribute("user");
    String backUrl = request.getContextPath() + "/secure/userMain"; // по умолчанию
    if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole().toString())) {
        backUrl = request.getContextPath() + "/secure/admin/adminMain";
    }
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>История прохождения тестов</title>
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
            max-width: 1000px;
            margin: 30px auto;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.4);
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .history-title {
            font-size: 28px;
            margin-bottom: 10px;
            color: #4fc3f7;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        th {
            background: rgba(255, 255, 255, 0.1);
            font-weight: 600;
        }

        tr:hover {
            background: rgba(255, 255, 255, 0.05);
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

        .score-cell {
            font-weight: bold;
        }

        .score-high {
            color: #81c784;
        }

        .score-medium {
            color: #ffd54f;
        }

        .score-low {
            color: #e57373;
        }
        .btn-back { background: #2196F3; }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <h1 class="history-title">История прохождения тестов</h1>
    </div>

    <c:choose>

        <c:when test="${empty userHistory}">
            <div style="text-align: center; padding: 30px;">
                <p>Вы еще не прошли ни одного теста</p>
                <a href="${pageContext.request.contextPath}/secure/tests">
                    <button class="btn-menu">Пройти тест</button>
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Дата прохождения</th>
                    <th>Название теста</th>
                    <th>Тема</th>
                    <th>Результат</th>
                </tr>
                <c:forEach var="record" items="${userHistory}">
                    <tr>
                        <td>${record.completionDate}</td>
                        <td>${record.testResult.testName}</td>
                        <td>${record.testResult.testTopic}</td>
                        <td class="score-cell
                            <c:choose>
                                <c:when test="${record.testResult.score >= 80}">score-high</c:when>
                                <c:when test="${record.testResult.score >= 60}">score-medium</c:when>
                                <c:otherwise>score-low</c:otherwise>
                            </c:choose>
                        ">${record.testResult.score}%</td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

    <div class="top-buttons">
        <form action="<%= backUrl %>" method="get">
            <button class="btn-back" type="submit">&larr; Назад</button>
        </form>
    </div>
</div>

</body>
</html>