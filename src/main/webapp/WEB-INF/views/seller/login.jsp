<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Seller Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/styles.css">
    <style>
        .glass {background: rgba(255,255,255,0.15);backdrop-filter: blur(10px);border-radius:12px;padding:2rem;max-width:400px;margin:5rem auto;box-shadow:0 4px 30px rgba(0,0,0,0.1);}        input, button {width:100%;margin-top:0.5rem;}
    </style>
</head>
<body>
<div class="glass">
    <h2 style="font-family:'Inter',sans-serif; text-align:center;">Seller Login</h2>
    <form method="post" action="${pageContext.request.contextPath}/seller/login">
        <input type="email" name="email" placeholder="Email" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit" class="btn-primary">Log In</button>
    </form>
    <p style="text-align:center; margin-top:1rem;">
        New here? <a href="${pageContext.request.contextPath}/seller/register">Register</a>
    </p>
</div>
</body>
</html>
