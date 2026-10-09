<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="My Orders — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 900px; margin: 2rem auto; padding: 2.5rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h2><i class="fa-solid fa-box-open"></i> My Order History</h2>
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline btn-sm">
            <i class="fa-solid fa-plus"></i> Shop More
        </a>
    </div>

    <c:choose>
        <c:when test="${empty orders}">
            <div style="text-align: center; padding: 3rem 1rem; color: var(--text-muted);">
                <i class="fa-solid fa-bag-shopping" style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.6; display: block;"></i>
                <h3>No Orders Found</h3>
                <p>You haven't placed any orders yet.</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary" style="margin-top: 1rem;">
                    Browse Marketplace
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 1.5rem;">
                <c:forEach var="order" items="${orders}">
                    <div style="background: rgba(255, 255, 255, 0.04); border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 12px; padding: 1.5rem; transition: transform 0.2s ease;">
                        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255, 255, 255, 0.08); padding-bottom: 0.75rem; margin-bottom: 1rem; flex-wrap: wrap; gap: 0.5rem;">
                            <div>
                                <strong style="font-size: 1.1rem; color: var(--accent-color);">Order #ORD-${order.id}</strong>
                                <span style="font-size: 0.85rem; color: var(--text-muted); margin-left: 0.75rem;">
                                    Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                                </span>
                            </div>
                            <div style="display: flex; align-items: center; gap: 1rem;">
                                <span class="badge badge-success" style="padding: 0.25rem 0.75rem; border-radius: 20px; font-weight: 600;">
                                    ${order.status}
                                </span>
                                <strong style="font-size: 1.2rem; color: #10b981;">$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00" /></strong>
                            </div>
                        </div>

                        <!-- Order Items -->
                        <div style="display: flex; flex-direction: column; gap: 0.5rem;">
                            <c:forEach var="item" items="${order.items}">
                                <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.02); padding: 0.6rem 1rem; border-radius: 8px;">
                                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                                        <c:if test="${not empty item.productImage}">
                                            <img src="${item.productImage}" alt="Product" style="width: 40px; height: 40px; object-fit: cover; border-radius: 6px;" />
                                        </c:if>
                                        <div>
                                            <div style="font-weight: 600;">
                                                <c:out value="${item.productName != null ? item.productName : 'Product #' + item.productId}" />
                                            </div>
                                            <div style="font-size: 0.8rem; color: var(--text-muted);">
                                                Qty: ${item.quantity} × $<fmt:formatNumber value="${item.unitPrice}" pattern="0.00" />
                                            </div>
                                        </div>
                                    </div>
                                    <div style="font-weight: 600;">
                                        $<fmt:formatNumber value="${item.unitPrice * item.quantity}" pattern="0.00" />
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
