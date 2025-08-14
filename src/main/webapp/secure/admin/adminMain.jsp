<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Админ-панель</title>
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
            max-width: 900px;
            margin: 50px auto;
            background: rgba(0,0,0,0.4);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
        }
        h2 { text-align: center; margin-bottom: 25px; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { padding: 10px; border: 1px solid rgba(255,255,255,0.3); text-align: center; }
        th { background: rgba(255,255,255,0.2); }
        button {
            padding: 6px 12px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            color: white;
            transition: all 0.3s ease;
        }
        .btn-create { background: #2196F3; }
        .btn-edit { background: #FFC107; }
        .btn-delete { background: #F44336; }
        .btn-stats { background: #4CAF50; }
        button:hover { transform: scale(1.05); }
        .top-buttons { display: flex; justify-content: space-between; margin-bottom: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Админ-панель: управление тестами</h2>

    <div class="top-buttons">
        <form action="<%= request.getContextPath() %>/secure/admin/testCreating" method="get">
            <button class="btn-create" type="submit">Создать новый тест</button>
        </form>
        <form action="${pageContext.request.contextPath}/viewStats" method="get">
            <button class="btn-stats" type="submit">Просмотр статистики</button>
        </form>
    </div>

    <table>
        <tr>
            <th>Название теста</th>
            <th>Тема</th>
            <th>Действия</th>
        </tr>
        <c:forEach var="test" items="${tests}">
            <tr>
                <td>${test.name}</td>
                <td>${test.topic}</td>
                <td>
                    <form style="display:inline;" action="${pageContext.request.contextPath}/editTest" method="get">
                        <input type="hidden" name="testId" value="${test.id}">
                        <button class="btn-edit" type="submit">Редактировать</button>
                    </form>
                    <form style="display:inline;" action="${pageContext.request.contextPath}/deleteTest" method="post" onsubmit="return confirm('Вы точно хотите удалить тест?');">
                        <input type="hidden" name="testId" value="${test.id}">
                        <button class="btn-delete" type="submit">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
