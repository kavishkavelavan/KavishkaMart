<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'KavishkaMart — Multi-Seller E-Commerce Marketplace'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-logo" id="nav-brand-link">
            <i class="fa-solid fa-store"></i> KavishkaMart
        </a>
        <nav>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/" class="nav-link" id="nav-home-link">Browse</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <c:if test="${sessionScope.currentUser.role == 'SELLER' || sessionScope.currentUser.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link" id="nav-seller-link">Seller Dashboard</a></li>
                        </c:if>
                        <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link" id="nav-admin-link">Admin Panel</a></li>
                        </c:if>
                        <li><a href="${pageContext.request.contextPath}/orders" class="nav-link" id="nav-orders-link">Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/wishlist" class="nav-link" id="nav-wishlist-link"><i class="fa-solid fa-heart" style="color:#ec4899;"></i> Wishlist</a></li>
                        <li><a href="${pageContext.request.contextPath}/cart" class="nav-link" id="nav-cart-link"><i class="fa-solid fa-cart-shopping"></i> Cart</a></li>
                        <li>
                            <span class="badge badge-${fn:toLowerCase(sessionScope.currentUser.role)}">
                                <c:out value="${sessionScope.currentUser.role}" />
                            </span>
                        </li>
                        <li><span style="color: var(--text-main); font-weight: 600;"><c:out value="${sessionScope.currentUser.name}" /></span></li>
                        <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm" id="nav-logout-btn">Logout</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm" id="nav-login-btn">Log In</a></li>
                        <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm" id="nav-register-btn">Sign Up</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </header>
    <main class="container">
