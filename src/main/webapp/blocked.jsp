<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Получаем время разблокировки из запроса
    LocalDateTime unblockTime = (LocalDateTime) request.getAttribute("unblockTime");
    String formattedTime = unblockTime != null ?
            unblockTime.format(DateTimeFormatter.ofPattern("HH:mm")) : "через 5 минут";
%>
<html>
<head>
    <title>Аккаунт временно заблокирован</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: linear-gradient(135deg, #1d1f21, #0f2027);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: white;
        }
        .container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            padding: 30px 40px;
            border-radius: 15px;
            text-align: center;
            max-width: 500px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
        }
        h1 {
            color: #ff5252;
            margin-top: 0;
        }
        .countdown {
            font-size: 24px;
            margin: 20px 0;
            font-weight: bold;
            color: #ffcc80;
        }
        .contact {
            margin-top: 25px;
            font-size: 14px;
            color: #b0bec5;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Слишком много попыток входа</h1>
    <p>Ваш аккаунт временно заблокирован из-за превышения количества попыток входа.</p>

    <div class="countdown">
        Доступ будет восстановлен в <%= formattedTime %>
    </div>

    <p>Пожалуйста, попробуйте позже.</p>


</div>
</body>
</html>