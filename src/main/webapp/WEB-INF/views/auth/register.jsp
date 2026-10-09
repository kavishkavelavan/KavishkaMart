<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Account — KavishkaMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="auth-wrapper">
    <div class="glass-card">
        <h2 style="font-size: 1.8rem; text-align: center; margin-bottom: 0.5rem;">Join KavishkaMart</h2>
        <p style="text-align: center; color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">
            Create a Buyer or Seller account to get started.
        </p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST" id="register-form">
            <div class="form-group">
                <label for="name" class="form-label">Full Name</label>
                <input type="text" id="name" name="name" class="form-control" placeholder="John Doe" value="<c:out value='${name}' />" required autocomplete="name" />
                <c:if test="${not empty fieldErrors.name}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.name}" /></span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" placeholder="john@example.com" value="<c:out value='${email}' />" required autocomplete="email" />
                <c:if test="${not empty fieldErrors.email}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.email}" /></span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password (Min. 6 characters)</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required autocomplete="new-password" minlength="6" />
                <c:if test="${not empty fieldErrors.password}">
                    <span style="color: var(--danger); font-size: 0.8rem;"><c:out value="${fieldErrors.password}" /></span>
                </c:if>
            </div>

            <div class="form-group">
                <label for="role" class="form-label">Account Role</label>
                <select id="role" name="role" class="form-select">
                    <option value="BUYER" ${role == 'BUYER' ? 'selected' : ''}>Buyer (Browse, Cart & Purchase)</option>
                    <option value="SELLER" ${role == 'SELLER' ? 'selected' : ''}>Seller (List Products & Manage Orders)</option>
                </select>
                <small style="color: var(--text-dim); display: block; margin-top: 0.25rem;">Note: Admin access is assigned via system seed accounts.</small>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;" id="register-submit-btn">
                <i class="fa-solid fa-user-plus"></i> Create Account
            </button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
            Already have an account? <a href="${pageContext.request.contextPath}/login" id="register-login-link">Sign In</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
