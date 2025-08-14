<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Главная страница</title>
    <style>
        /* Сразу убираем белый фон */
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            background-color: #1d1f21; /* запасной фон на случай задержки CSS */
            font-family: 'Segoe UI', Tahoma, sans-serif;
            color: white;
        }

        body {
            background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364);
            background-size: 300% 300%;
            animation: gradient 12s ease infinite;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .dashboard-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.4);
            width: 350px;
            text-align: center;
            animation: fadeIn 0.4s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-5px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .dashboard-container h2 {
            margin-bottom: 25px;
            font-weight: 500;
        }

        .dashboard-container button {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            transition: all 0.3s ease;
            color: white;
        }

        .btn-tests { background: linear-gradient(135deg, #4CAF50, #2e7d32); }
        .btn-tests:hover { background: linear-gradient(135deg, #66bb6a, #388e3c); transform: scale(1.02); }

        .btn-results { background: linear-gradient(135deg, #2196F3, #1565c0); }
        .btn-results:hover { background: linear-gradient(135deg, #42a5f5, #1976d2); transform: scale(1.02); }

        .btn-history { background: linear-gradient(135deg, #FF9800, #f57c00); }
        .btn-history:hover { background: linear-gradient(135deg, #ffb74d, #ef6c00); transform: scale(1.02); }

        .btn-logout { background: linear-gradient(135deg, #F44336, #c62828); }
        .btn-logout:hover { background: linear-gradient(135deg, #e57373, #d32f2f); transform: scale(1.02); }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h2>Добро пожаловать!</h2>

    <form action="<%= request.getContextPath() %>/secure/tests" method="get">
        <button type="submit" class="btn-tests">Тесты</button>
    </form>

    <form action="<%= request.getContextPath() %>/secure/myResults" method="get">
        <button type="submit" class="btn-results">Мои результаты</button>
    </form>

    <form action="<%= request.getContextPath() %>/secure/history" method="get">
        <button type="submit" class="btn-history">История</button>
    </form>

    <form action="<%= request.getContextPath() %>/secure/logout" method="post">
        <button type="submit" class="btn-logout">Выйти</button>
    </form>
</div>
</body>
</html>
