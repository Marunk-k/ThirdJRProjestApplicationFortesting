<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background: linear-gradient(135deg, #1d1f21, #0f2027, #203a43, #2c5364);
            background-size: 300% 300%;
            animation: gradient 12s ease infinite;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        .form-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.4);
            width: 320px;
            text-align: center;
            color: white;
            animation: fadeIn 0.6s ease;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .form-container h2 {
            margin-bottom: 20px;
            font-weight: 500;
        }
        .form-container input {
            width: 100%;
            padding: 12px;
            margin: 8px 0;
            border-radius: 8px;
            border: none;
            outline: none;
            font-size: 14px;
            background: rgba(255, 255, 255, 0.15);
            color: white;
            transition: background 0.3s, border 0.3s;
        }
        .form-container input::placeholder {
            color: rgba(255, 255, 255, 0.6);
        }
        .form-container input:focus {
            background: rgba(255, 255, 255, 0.25);
        }
        .login-btn, .register-btn {
            width: 100%;
            padding: 12px;
            margin-top: 10px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            transition: all 0.3s ease;
        }
        .login-btn {
            background: linear-gradient(135deg, #4CAF50, #2e7d32);
            color: white;
        }
        .login-btn:hover {
            background: linear-gradient(135deg, #66bb6a, #388e3c);
            transform: scale(1.02);
        }
        .register-btn {
            background: linear-gradient(135deg, #2196F3, #1565c0);
            color: white;
        }
        .register-btn:hover {
            background: linear-gradient(135deg, #42a5f5, #1976d2);
            transform: scale(1.02);
        }
        .error-input {
            border: 1px solid #ff5757 !important;
            background: rgba(255, 87, 87, 0.1) !important;
        }
    </style>

    <script>
        // Функция для проверки и отображения ошибки
        function checkForError() {
            const errorMessage = "<%= session.getAttribute("registerError") %>";

            if (errorMessage && errorMessage !== "null") {
                // Показываем всплывающее окно
                alert(errorMessage);

                // Подсвечиваем поле логина
                document.querySelector('input[name="login"]').classList.add('error-input');

                // Очищаем ошибку из сессии
                fetch('/clearRegisterError', { method: 'POST' });
            }
        }

        // Вызываем при загрузке страницы
        window.onload = checkForError;
    </script>
</head>
<body>

<div class="form-container">
    <h2>Регистрация</h2>

    <form action="/register" method="post">
        <input type="text" name="login" placeholder="Логин" required>
        <input type="password" name="password" placeholder="Пароль" required>
        <button type="submit" class="register-btn">Зарегистрироваться</button>
    </form>

    <form action="/login" method="get">
        <button type="submit" class="login-btn">У меня уже есть аккаунт</button>
    </form>
</div>

</body>
</html>