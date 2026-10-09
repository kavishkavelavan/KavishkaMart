<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Order Confirmation — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 700px; margin: 3rem auto; padding: 2.5rem; text-align: center;">
    <div style="font-size: 3.5rem; color: #10b981; margin-bottom: 1rem;">
        <i class="fa-solid fa-circle-check"></i>
    </div>
    <h1 style="margin-bottom: 0.5rem; font-weight: 700;">Order Placed Successfully!</h1>
    <p style="color: var(--text-muted); margin-bottom: 2rem;">Thank you for shopping with KavishkaMart. Your order has been confirmed and is being processed.</p>

    <c:if test="${not empty order}">
        <div style="background: rgba(255, 255, 255, 0.05); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 12px; padding: 1.5rem; text-align: left; margin-bottom: 2rem;">
            <div style="display: flex; justify-content: space-between; border-bottom: 1px solid rgba(255, 255, 255, 0.1); padding-bottom: 0.75rem; margin-bottom: 1rem;">
                <div>
                    <span style="color: var(--text-muted); font-size: 0.9rem; display: block;">Order Reference</span>
                    <strong style="font-size: 1.1rem; color: var(--accent-color);">#ORD-${order.id}</strong>
                </div>
                <div style="text-align: right;">
                    <span style="color: var(--text-muted); font-size: 0.9rem; display: block;">Status</span>
                    <span class="badge badge-success" style="padding: 0.25rem 0.75rem; border-radius: 20px; font-weight: 600;">
                        ${order.status}
                    </span>
                </div>
            </div>

            <h3 style="font-size: 1rem; margin-bottom: 0.75rem; color: var(--text-main);">Order Items</h3>
            <table class="table" style="width: 100%; border-collapse: collapse; margin-bottom: 1rem;">
                <thead>
                    <tr style="border-bottom: 1px solid rgba(255,255,255,0.1); color: var(--text-muted); font-size: 0.85rem;">
                        <th style="text-align: left; padding: 0.5rem 0;">Product</th>
                        <th style="text-align: center; padding: 0.5rem 0;">Qty</th>
                        <th style="text-align: right; padding: 0.5rem 0;">Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                            <td style="padding: 0.5rem 0;">
                                <c:out value="${item.productName != null ? item.productName : 'Product #' + item.productId}" />
                            </td>
                            <td style="text-align: center; padding: 0.5rem 0;">${item.quantity}</td>
                            <td style="text-align: right; padding: 0.5rem 0;">$<fmt:formatNumber value="${item.unitPrice * item.quantity}" pattern="0.00" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid rgba(255, 255, 255, 0.1); padding-top: 0.75rem;">
                <span style="font-weight: 600;">Total Paid</span>
                <strong style="font-size: 1.25rem; color: #10b981;">$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00" /></strong>
            </div>
        </div>
    </c:if>

    <div style="display: flex; gap: 1rem; justify-content: center;">
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
            <i class="fa-solid fa-receipt"></i> View Order History
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
            <i class="fa-solid fa-store"></i> Back to Store
        </a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
