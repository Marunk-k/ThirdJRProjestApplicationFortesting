<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Set" %>
<%@ page import="entity.User" %>
<%@ page isELIgnored="false" %>
<%
    User currentUser = (User) session.getAttribute("user");
    String backUrl = request.getContextPath() + "/secure/userMain";
    if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole().toString())) {
        backUrl = request.getContextPath() + "/secure/admin/adminMain";
    }
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Главная - Тесты</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364);
            background-size: 300% 300%;
            animation: gradient 12s ease infinite;
            margin: 0;
            padding: 0;
        }

        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.4);
            color: white;
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        table, th, td {
            border: 1px solid rgba(255,255,255,0.3);
        }

        th, td {
            padding: 10px;
            text-align: center;
        }

        th {
            background: rgba(255,255,255,0.2);
        }

        button {
            padding: 8px 15px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            color: white;
            background: linear-gradient(135deg, #4CAF50, #2e7d32);
            transition: all 0.3s ease;
        }

        button:hover {
            background: linear-gradient(135deg, #66bb6a, #388e3c);
            transform: scale(1.05);
        }

        .top-buttons {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .top-buttons form {
            display: inline-block;
        }

        .filter-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 10px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        select {
            padding: 8px 15px;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.15);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
            outline: none;
            min-width: 200px;
        }

        option {
            background: #2c5364;
            color: white;
        }

        .btn-back {
            background: linear-gradient(135deg, #2196F3, #0d47a1) !important;
        }

        .btn-back:hover {
            background: linear-gradient(135deg, #42a5f5, #1565c0) !important;
        }

        .btn-filter {
            background: linear-gradient(135deg, #FF9800, #e65100) !important;
        }

        .btn-filter:hover {
            background: linear-gradient(135deg, #ffb74d, #f57c00) !important;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="top-buttons">
        <form action="<%= backUrl %>" method="get">
            <button class="btn-back" type="submit">&larr; Назад</button>
        </form>
    </div>

    <h3>Доступные тесты</h3>

    <div class="filter-section">
        <div class="filter-group">
            <form action="<%= request.getContextPath() %>/secure/tests" method="get">
                <select name="topicFilter" onchange="this.form.submit()">
                    <option value="">Все категории</option>
                    <c:forEach items="${uniqueTopics}" var="topic">
                        <option value="${topic}"
                            ${currentFilter eq topic ? 'selected' : ''}>
                                ${topic}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </div>

        <c:if test="${not empty currentFilter}">
            <form action="<%= request.getContextPath() %>/secure/tests" method="get">
                <button class="btn-filter" type="submit">Сбросить фильтр</button>
            </form>
        </c:if>
    </div>

    <table>
        <tr>
            <th>Название теста</th>
            <th>Тема</th>
            <th>Пройти тест</th>
        </tr>
        <c:choose>
            <c:when test="${empty tests}">
                <tr>
                    <td colspan="3">Тесты не найдены!</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="test" items="${tests}">
                    <tr>
                        <td>${test.name}</td>
                        <td>${test.topic}</td>
                        <td>
                            <form action="<%= request.getContextPath() %>/secure/testPassing" method="get">
                                <input type="hidden" name="testId" value="${test.id}">
                                <button type="submit">Начать</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</div>

</body>
</html>