<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Marketplace Products — KavishkaMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div style="text-align: center; margin: 2rem 0;">
    <h1 style="font-size: 2.5rem; margin-bottom: 0.5rem; background: var(--accent-gradient); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
        KavishkaMart Catalog
    </h1>
    <p style="color: var(--text-muted); font-size: 1rem;">
        Discover verified listings, compare prices, and read verified customer reviews.
    </p>
</div>

<div class="glass-card">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
        <h2>All Products</h2>
        <span class="badge badge-buyer">Live Catalog</span>
    </div>

    <!-- Search & Filter Bar -->
    <form action="${pageContext.request.contextPath}/products" method="GET" style="display: flex; gap: 1rem; flex-wrap: wrap; background: rgba(255, 255, 255, 0.04); padding: 1.25rem; border-radius: 12px; margin-bottom: 2rem; border: 1px solid rgba(255, 255, 255, 0.08); align-items: center;">
        <div style="flex: 2; min-width: 220px; position: relative;">
            <input type="text" name="q" value="<c:out value='${searchQuery}' />" placeholder="Search by product name or description..." class="form-control" style="width: 100%; padding: 0.6rem 1rem 0.6rem 2.5rem; background: rgba(15, 23, 42, 0.8); color: white; border: 1px solid var(--border-color); border-radius: 8px;" />
            <i class="fa-solid fa-magnifying-glass" style="position: absolute; left: 0.9rem; top: 50%; transform: translateY(-50%); color: var(--text-muted);"></i>
        </div>

        <div style="flex: 1; min-width: 180px;">
            <select name="category" class="form-control" style="width: 100%; padding: 0.6rem 1rem; background: rgba(15, 23, 42, 0.8); color: white; border: 1px solid var(--border-color); border-radius: 8px;">
                <option value="ALL" ${selectedCategory == 'ALL' ? 'selected' : ''}>All Categories</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat}" ${selectedCategory == cat ? 'selected' : ''}>${cat}</option>
                </c:forEach>
            </select>
        </div>

        <div style="display: flex; gap: 0.5rem;">
            <button type="submit" class="btn btn-primary" style="padding: 0.6rem 1.25rem;">
                <i class="fa-solid fa-filter"></i> Filter
            </button>
            <c:if test="${not empty searchQuery || (not empty selectedCategory && selectedCategory != 'ALL')}">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline" style="padding: 0.6rem 1rem;">
                    Clear
                </a>
            </c:if>
        </div>
    </form>

    <div id="product-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem;">
        <c:forEach var="p" items="${products}">
            <div style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; display: flex; flex-direction: column; justify-content: space-between;">
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
                        <button class="btn btn-outline btn-sm" onclick="openReviewModal(${p.id}, '${p.name}')">
                            <i class="fa-solid fa-star" style="color: #f59e0b;"></i> Reviews
                        </button>
                    </div>

                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser}">
                            <button onclick="addToCart(${p.id})" class="btn btn-primary" style="width: 100%;" ${p.stockQty <= 0 ? 'disabled' : ''}>
                                <i class="fa-solid fa-cart-plus"></i> Add to Cart
                            </button>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary" style="width: 100%;">
                                Login to Purchase
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<!-- Review Modal -->
<div id="review-modal" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); backdrop-filter: blur(5px); z-index: 9999; justify-content: center; align-items: center;">
    <div class="glass-card" style="max-width: 550px; width: 90%; max-height: 85vh; overflow-y: auto; padding: 2rem; position: relative; border: 1px solid rgba(255,255,255,0.2);">
        <button onclick="closeReviewModal()" style="position: absolute; top: 1rem; right: 1rem; background: none; border: none; color: var(--text-main); font-size: 1.5rem; cursor: pointer;">&times;</button>
        
        <h3 id="modal-product-title" style="margin-bottom: 0.5rem;">Product Reviews</h3>
        <div id="modal-rating-summary" style="font-size: 1.1rem; margin-bottom: 1.5rem; color: #f59e0b; font-weight: 600;">
            <i class="fa-solid fa-star"></i> <span id="avg-rating-val">0.0</span> / 5.0 (<span id="total-reviews-val">0</span> reviews)
        </div>

        <!-- Add Review Form (Logged in users only) -->
        <c:if test="${not empty sessionScope.currentUser}">
            <div style="background: rgba(255,255,255,0.05); padding: 1.25rem; border-radius: 8px; margin-bottom: 1.5rem; border: 1px solid rgba(255,255,255,0.1);">
                <h4 style="margin-bottom: 0.75rem;">Write a Review</h4>
                <form id="review-form" onsubmit="submitReview(event)">
                    <input type="hidden" id="review-product-id" name="productId" />
                    <div style="margin-bottom: 1rem;">
                        <label style="display: block; font-size: 0.85rem; margin-bottom: 0.4rem; color: var(--text-muted);">Rating (Stars)</label>
                        <select id="review-rating" class="form-control" style="width: 100%; background: rgba(15,23,42,0.8); color: white; padding: 0.5rem; border-radius: 6px; border: 1px solid var(--border-color);" required>
                            <option value="5">⭐⭐⭐⭐⭐ (5 - Excellent)</option>
                            <option value="4">⭐⭐⭐⭐ (4 - Good)</option>
                            <option value="3">⭐⭐⭐ (3 - Average)</option>
                            <option value="2">⭐⭐ (2 - Poor)</option>
                            <option value="1">⭐ (1 - Terrible)</option>
                        </select>
                    </div>
                    <div style="margin-bottom: 1rem;">
                        <label style="display: block; font-size: 0.85rem; margin-bottom: 0.4rem; color: var(--text-muted);">Comment</label>
                        <textarea id="review-comment" class="form-control" rows="3" placeholder="Share details of your experience with this product..." style="width: 100%; background: rgba(15,23,42,0.8); color: white; padding: 0.5rem; border-radius: 6px; border: 1px solid var(--border-color);" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm" style="width: 100%;">
                        <i class="fa-solid fa-paper-plane"></i> Submit Review
                    </button>
                </form>
            </div>
        </c:if>

        <!-- Review List -->
        <h4 style="margin-bottom: 1rem;">Customer Feedback</h4>
        <div id="reviews-list-container" style="display: flex; flex-direction: column; gap: 1rem;">
            <div style="color: var(--text-muted); text-align: center;">Loading reviews...</div>
        </div>
    </div>
</div>

<script>
    let currentModalProductId = null;

    async function addToCart(productId) {
        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/cart?action=add', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: `productId=\${productId}&quantity=1`
            });
            if (resp.ok) {
                alert('Item added to shopping cart!');
            } else {
                alert('Failed to add item to cart (Status: ' + resp.status + ')');
            }
        } catch (e) {
            alert('Failed to add item to cart');
        }
    }

    async function openReviewModal(productId, productName) {
        currentModalProductId = productId;
        document.getElementById('review-product-id').value = productId;
        document.getElementById('modal-product-title').textContent = productName + ' — Customer Reviews';
        document.getElementById('review-modal').style.display = 'flex';
        await loadReviews(productId);
    }

    function closeReviewModal() {
        document.getElementById('review-modal').style.display = 'none';
    }

    async function loadReviews(productId) {
        const container = document.getElementById('reviews-list-container');
        container.innerHTML = '<div style="color: var(--text-muted); text-align: center;">Loading reviews...</div>';

        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/reviews?productId=' + productId);
            const result = await resp.json();

            if (result.success && result.data) {
                const data = result.data;
                document.getElementById('avg-rating-val').textContent = data.averageRating.toFixed(1);
                document.getElementById('total-reviews-val').textContent = data.totalReviews;

                const reviews = data.reviews;
                if (!reviews || reviews.length === 0) {
                    container.innerHTML = '<div style="color: var(--text-muted); text-align: center; padding: 1rem;">No reviews yet. Be the first to review!</div>';
                    return;
                }

                container.innerHTML = '';
                reviews.forEach(r => {
                    const stars = '★'.repeat(r.rating) + '☆'.repeat(5 - r.rating);
                    const userName = r.userName || 'Anonymous Buyer';
                    const dateStr = new Date(r.createdAt).toLocaleDateString();
                    const card = '<div style="background: rgba(255,255,255,0.03); padding: 1rem; border-radius: 8px; border: 1px solid rgba(255,255,255,0.08);">' +
                            '<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.4rem;">' +
                                '<strong>' + userName + '</strong>' +
                                '<span style="color: #f59e0b; font-size: 0.9rem;">' + stars + '</span>' +
                            '</div>' +
                            '<p style="margin: 0; font-size: 0.9rem; color: var(--text-main);">' + r.comment + '</p>' +
                            '<span style="font-size: 0.75rem; color: var(--text-muted); margin-top: 0.5rem; display: block;">' +
                                dateStr +
                            '</span>' +
                        '</div>';
                    container.insertAdjacentHTML('beforeend', card);
                });
            }
        } catch (e) {
            container.innerHTML = '<div style="color: #ef4444; text-align: center;">Failed to load reviews.</div>';
        }
    }

    async function submitReview(event) {
        event.preventDefault();
        const productId = document.getElementById('review-product-id').value;
        const rating = document.getElementById('review-rating').value;
        const comment = document.getElementById('review-comment').value;

        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/reviews', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: 'productId=' + productId + '&rating=' + rating + '&comment=' + encodeURIComponent(comment)
            });

            const result = await resp.json();
            if (result.success) {
                document.getElementById('review-comment').value = '';
                await loadReviews(productId);
            } else {
                alert(result.error ? result.error.message : 'Failed to submit review');
            }
        } catch (e) {
            alert('An error occurred submitting your review.');
        }
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
