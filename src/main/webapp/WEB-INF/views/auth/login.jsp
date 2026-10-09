<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Log In — KavishkaMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="auth-wrapper">
    <div class="glass-card">
        <h2 style="font-size: 1.8rem; text-align: center; margin-bottom: 0.5rem;">Welcome Back</h2>
        <p style="text-align: center; color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">
            Log in to your KavishkaMart buyer, seller, or admin account.
        </p>

        <c:if test="${param.logged_out == 'true'}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> You have been logged out successfully.
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST" id="login-form">
            <c:if test="${not empty param.redirect}">
                <input type="hidden" name="redirect" value="<c:out value='${param.redirect}' />" />
            </c:if>

            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="user@example.com" value="<c:out value='${email}' />" required autocomplete="email" />
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required autocomplete="current-password" />
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;" id="login-submit-btn">
                <i class="fa-solid fa-right-to-bracket"></i> Sign In
            </button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
            Don't have an account yet? <a href="${pageContext.request.contextPath}/register" id="login-signup-link">Create Account</a>
        </div>

        <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color); font-size: 0.8rem; color: var(--text-dim);">
            <p><strong>Demo Seed Accounts:</strong></p>
            <ul>
                <li>Admin: <code>admin@kavishkamart.com</code></li>
                <li>Seller: <code>seller1@kavishkamart.com</code></li>
                <li>Buyer: <code>buyer1@kavishkamart.com</code></li>
            </ul>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
