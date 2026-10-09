<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="Admin Panel — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div style="max-width: 1000px; margin: 2rem auto;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h2><i class="fa-solid fa-user-shield"></i> System Administration Panel</h2>
        <a href="${pageContext.request.contextPath}/api/v1/health" target="_blank" class="btn btn-outline btn-sm">
            <i class="fa-solid fa-heart-pulse"></i> Check Health API
        </a>
    </div>

    <!-- Metrics Cards Grid -->
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1.5rem; margin-bottom: 2.5rem;">
        <div class="glass-card" style="padding: 1.5rem; text-align: center;">
            <div style="font-size: 2.5rem; color: #4a90e2; margin-bottom: 0.5rem;"><i class="fa-solid fa-users"></i></div>
            <div style="font-size: 2rem; font-weight: 700;">${userCount}</div>
            <div style="color: var(--text-muted); font-size: 0.9rem;">Registered Users</div>
        </div>

        <div class="glass-card" style="padding: 1.5rem; text-align: center;">
            <div style="font-size: 2.5rem; color: #f59e0b; margin-bottom: 0.5rem;"><i class="fa-solid fa-boxes-packing"></i></div>
            <div style="font-size: 2rem; font-weight: 700;">${productCount}</div>
            <div style="color: var(--text-muted); font-size: 0.9rem;">Active Products</div>
        </div>

        <div class="glass-card" style="padding: 1.5rem; text-align: center;">
            <div style="font-size: 2.5rem; color: #10b981; margin-bottom: 0.5rem;"><i class="fa-solid fa-cart-check"></i></div>
            <div style="font-size: 2rem; font-weight: 700;">${orderCount}</div>
            <div style="color: var(--text-muted); font-size: 0.9rem;">Total Orders</div>
        </div>

        <div class="glass-card" style="padding: 1.5rem; text-align: center;">
            <div style="font-size: 2.5rem; color: #ec4899; margin-bottom: 0.5rem;"><i class="fa-solid fa-server"></i></div>
            <div style="font-size: 1.5rem; font-weight: 700; color: #10b981; margin-top: 0.4rem;">HEALTHY</div>
            <div style="color: var(--text-muted); font-size: 0.9rem;">Database & H2 Pool</div>
        </div>
    </div>

    <!-- User Management Table -->
    <div class="glass-card" style="padding: 2rem;">
        <h3 style="margin-bottom: 1.5rem;"><i class="fa-solid fa-users-gear"></i> System User Accounts</h3>
        <div class="table-container" style="overflow-x: auto;">
            <table class="table" style="width: 100%; border-collapse: collapse; text-align: left;">
                <thead>
                    <tr style="border-bottom: 2px solid rgba(255,255,255,0.1);">
                        <th style="padding: 0.75rem;">ID</th>
                        <th style="padding: 0.75rem;">Name</th>
                        <th style="padding: 0.75rem;">Email</th>
                        <th style="padding: 0.75rem;">Role</th>
                        <th style="padding: 0.75rem;">Registered On</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${userList}">
                        <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                            <td style="padding: 0.75rem;">#${u.id}</td>
                            <td style="padding: 0.75rem; font-weight: 600;"><c:out value="${u.name}" /></td>
                            <td style="padding: 0.75rem;"><c:out value="${u.email}" /></td>
                            <td style="padding: 0.75rem;">
                                <span class="badge badge-${fn:toLowerCase(u.role)}">
                                    <c:out value="${u.role}" />
                                </span>
                            </td>
                            <td style="padding: 0.75rem; color: var(--text-muted);">
                                <fmt:formatDate value="${u.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
