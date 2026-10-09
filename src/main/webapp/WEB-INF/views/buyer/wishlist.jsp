<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="My Saved Wishlist — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 950px; margin: 2rem auto; padding: 2.5rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
        <h2><i class="fa-solid fa-heart" style="color: #ec4899;"></i> My Saved Wishlist</h2>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">
            <i class="fa-solid fa-plus"></i> Browse Products
        </a>
    </div>

    <c:choose>
        <c:when test="${empty wishlist}">
            <div style="text-align: center; padding: 3rem 1rem; color: var(--text-muted);">
                <i class="fa-solid fa-heart-crack" style="font-size: 3.5rem; margin-bottom: 1rem; opacity: 0.5; display: block; color: #ec4899;"></i>
                <h3>Your Wishlist is Empty</h3>
                <p>Save your favorite items here to purchase later!</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-top: 1rem;">
                    Browse Marketplace
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem;">
                <c:forEach var="p" items="${wishlist}">
                    <div id="wishlist-item-${p.id}" style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; display: flex; flex-direction: column; justify-content: space-between;">
                        <div>
                            <img src="${not empty p.imageUrl ? p.imageUrl : 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500'}" alt="${p.name}" style="width: 100%; height: 180px; object-fit: cover; border-radius: var(--radius-sm); margin-bottom: 1rem;">
                            
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                                <span class="badge badge-seller" style="font-size: 0.7rem;">${p.category}</span>
                                <span style="font-size: 0.8rem; color: ${p.stockQty > 0 ? '#10b981' : '#ef4444'}; font-weight: 600;">
                                    <c:choose>
                                        <c:when test="${p.stockQty > 0}">In Stock (${p.stockQty})</c:when>
                                        <c:otherwise>Out of Stock</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <h3 style="font-size: 1.1rem; margin: 0.5rem 0;"><c:out value="${p.name}" /></h3>
                            <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;"><c:out value="${p.description}" /></p>
                        </div>

                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                                <span style="font-size: 1.25rem; font-weight: 700; color: var(--accent);">$<fmt:formatNumber value="${p.price}" pattern="0.00" /></span>
                                <button onclick="removeFromWishlist(${p.id})" class="btn btn-danger btn-sm">
                                    <i class="fa-solid fa-trash-can"></i> Remove
                                </button>
                            </div>

                            <button onclick="addToCart(${p.id})" class="btn btn-primary" style="width: 100%;" ${p.stockQty <= 0 ? 'disabled' : ''}>
                                <i class="fa-solid fa-cart-plus"></i> Add to Cart
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    async function addToCart(productId) {
        try {
            await fetch('${pageContext.request.contextPath}/api/cart?action=add', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: `productId=\${productId}&quantity=1`
            });
            alert('Item added to shopping cart!');
        } catch (e) {
            alert('Failed to add item to cart');
        }
    }

    async function removeFromWishlist(productId) {
        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/wishlist', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: `action=remove&productId=\${productId}`
            });
            const res = await resp.json();
            if (res.success) {
                const el = document.getElementById(`wishlist-item-\${productId}`);
                if (el) el.remove();
            }
        } catch (e) {
            alert('Failed to remove item from wishlist');
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
