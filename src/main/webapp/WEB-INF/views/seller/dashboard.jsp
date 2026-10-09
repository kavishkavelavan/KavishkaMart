<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Seller Dashboard — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 950px; margin: 2rem auto; padding: 2.5rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h2><i class="fa-solid fa-store"></i> Seller Dashboard</h2>
        <div style="display: flex; gap: 0.75rem;">
            <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-secondary">
                <i class="fa-solid fa-truck-ramp-box"></i> Manage Orders
            </a>
            <a href="${pageContext.request.contextPath}/seller/product/new" class="btn btn-primary">
                <i class="fa-solid fa-plus"></i> Add New Product
            </a>
        </div>
    </div>

    <h3 style="margin-bottom: 1rem;"><i class="fa-solid fa-boxes-stacked"></i> Your Product Listings</h3>

    <div class="table-container" style="overflow-x: auto;">
        <table class="table" style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead>
                <tr style="border-bottom: 2px solid rgba(255,255,255,0.1);">
                    <th style="padding: 0.75rem;">Name</th>
                    <th style="padding: 0.75rem;">Category</th>
                    <th style="padding: 0.75rem;">Price</th>
                    <th style="padding: 0.75rem;">Stock Qty</th>
                    <th style="padding: 0.75rem; text-align: right;">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 2rem; color: var(--text-muted);">
                                You haven't added any products yet. Click <strong>Add New Product</strong> to start selling!
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="product" items="${products}">
                            <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                                <td style="padding: 0.75rem; font-weight: 600;"><c:out value="${product.name}" /></td>
                                <td style="padding: 0.75rem;"><span class="badge badge-seller"><c:out value="${product.category}" /></span></td>
                                <td style="padding: 0.75rem;">$<fmt:formatNumber value="${product.price}" pattern="0.00" /></td>
                                <td style="padding: 0.75rem; font-weight: 600; color: ${product.stockQty > 0 ? '#10b981' : '#ef4444'};">
                                    ${product.stockQty}
                                </td>
                                <td style="padding: 0.75rem; text-align: right;">
                                    <a href="${pageContext.request.contextPath}/seller/product/edit?id=${product.id}" class="btn btn-outline btn-sm">Edit</a>
                                    <form method="post" action="${pageContext.request.contextPath}/seller/product/delete" style="display:inline; margin: 0;">
                                        <input type="hidden" name="id" value="${product.id}" />
                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Delete this product?')">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
