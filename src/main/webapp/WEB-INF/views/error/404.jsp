<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found — KavishkaMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div style="text-align: center; margin: 5rem 0;">
    <h1 style="font-size: 5rem; color: var(--accent); margin-bottom: 0.5rem;">404</h1>
    <h2>Page Not Found</h2>
    <p style="color: var(--text-muted); margin: 1rem 0 2rem 0;">The page or resource you requested could not be found on KavishkaMart.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary" id="error-404-home-btn">Return to Home Page</a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
