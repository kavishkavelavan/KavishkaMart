<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Server Error — KavishkaMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div style="text-align: center; margin: 5rem 0;">
    <h1 style="font-size: 5rem; color: var(--danger); margin-bottom: 0.5rem;">500</h1>
    <h2>Internal Server Error</h2>
    <p style="color: var(--text-muted); margin: 1rem 0 2rem 0;">An unexpected error occurred while processing your request. Please try again later.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary" id="error-500-home-btn">Return to Safety</a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
