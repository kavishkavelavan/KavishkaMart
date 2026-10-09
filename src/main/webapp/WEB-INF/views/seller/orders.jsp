<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Seller Order Fulfillment — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 950px; margin: 2rem auto; padding: 2.5rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h2><i class="fa-solid fa-truck-ramp-box"></i> Seller Order Fulfillment</h2>
        <a href="${pageContext.request.contextPath}/seller/dashboard" class="btn btn-outline btn-sm">
            <i class="fa-solid fa-gauge-high"></i> Dashboard
        </a>
    </div>

    <c:choose>
        <c:when test="${empty orders}">
            <div style="text-align: center; padding: 3rem 1rem; color: var(--text-muted);">
                <i class="fa-solid fa-box-open" style="font-size: 3rem; margin-bottom: 1rem; opacity: 0.6; display: block;"></i>
                <h3>No Orders Received</h3>
                <p>When buyers purchase your products, their orders will appear here for processing.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 1.5rem;">
                <c:forEach var="order" items="${orders}">
                    <div style="background: rgba(255, 255, 255, 0.04); border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 12px; padding: 1.5rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255, 255, 255, 0.08); padding-bottom: 0.75rem; margin-bottom: 1rem; flex-wrap: wrap; gap: 0.5rem;">
                            <div>
                                <strong style="font-size: 1.1rem; color: var(--accent-color);">Order #ORD-${order.id}</strong>
                                <span style="font-size: 0.85rem; color: var(--text-muted); margin-left: 0.75rem;">
                                    Buyer ID: #${order.buyerId} | Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                                </span>
                            </div>

                            <!-- Update Order Status Form -->
                            <form action="${pageContext.request.contextPath}/seller/orders/update" method="POST" style="display: flex; align-items: center; gap: 0.5rem; margin: 0;">
                                <input type="hidden" name="orderId" value="${order.id}" />
                                <select name="status" class="form-control" style="padding: 0.35rem 0.75rem; border-radius: 6px; background: rgba(15,23,42,0.8); color: white; border: 1px solid var(--border-color);">
                                    <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                    <option value="CONFIRMED" ${order.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                    <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                    <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                    <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                </select>
                                <button type="submit" class="btn btn-primary btn-sm">
                                    <i class="fa-solid fa-floppy-disk"></i> Update
                                </button>
                            </form>
                        </div>

                        <!-- Purchased Products List -->
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
                                                Qty Ordered: ${item.quantity} × $<fmt:formatNumber value="${item.unitPrice}" pattern="0.00" />
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
