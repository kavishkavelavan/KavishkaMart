<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="KavishkaMart — Discover & Shop Quality Products" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div style="text-align: center; margin: 3rem 0;">
    <h1 style="font-size: 3rem; margin-bottom: 1rem; background: var(--accent-gradient); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
        Welcome to KavishkaMart
    </h1>
    <p style="font-size: 1.2rem; color: var(--text-muted); max-width: 650px; margin: 0 auto 2rem auto;">
        The next-generation multi-seller marketplace. Discover top electronics, apparel, and home gear with verified buyer reviews.
    </p>

    <div style="display: flex; justify-content: center; gap: 1rem;">
        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" id="hero-browse-btn">
            <i class="fa-solid fa-store"></i> Browse Full Catalog
        </a>
        <c:if test="${empty sessionScope.currentUser}">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary" id="hero-join-btn">Sign Up Now</a>
        </c:if>
    </div>
</div>

<div class="glass-card" style="margin-top: 2rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
        <h2>Featured Marketplace Listings</h2>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">View All</a>
    </div>

    <div id="featured-grid" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem;">
        <!-- Featured static preview cards -->
        <div style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; display: flex; flex-direction: column; justify-content: space-between;">
            <div>
                <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500" alt="Headphones" style="width: 100%; height: 180px; object-fit: cover; border-radius: var(--radius-sm); margin-bottom: 1rem;">
                <span class="badge badge-seller" style="font-size: 0.7rem;">Electronics</span>
                <h3 style="font-size: 1.1rem; margin: 0.5rem 0;">Wireless Noise-Canceling Headphones</h3>
                <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;">30-hour battery life with active noise cancellation.</p>
            </div>
            <div>
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <span style="font-size: 1.25rem; font-weight: 700; color: var(--accent);">$199.99</span>
                    <span style="color: #f59e0b; font-weight: 600;"><i class="fa-solid fa-star"></i> 4.8</span>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="width: 100%;">
                    Browse in Catalog
                </a>
            </div>
        </div>

        <div style="background: rgba(15, 23, 42, 0.6); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.25rem; display: flex; flex-direction: column; justify-content: space-between;">
            <div>
                <img src="https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500" alt="Mechanical Keyboard" style="width: 100%; height: 180px; object-fit: cover; border-radius: var(--radius-sm); margin-bottom: 1rem;">
                <span class="badge badge-seller" style="font-size: 0.7rem;">Electronics</span>
                <h3 style="font-size: 1.1rem; margin: 0.5rem 0;">Mechanical Gaming Keyboard</h3>
                <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 1rem;">Tactile blue switches with customizable RGB per-key backlight.</p>
            </div>
            <div>
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                    <span style="font-size: 1.25rem; font-weight: 700; color: var(--accent);">$89.50</span>
                    <span style="color: #f59e0b; font-weight: 600;"><i class="fa-solid fa-star"></i> 5.0</span>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="width: 100%;">
                    Browse in Catalog
                </a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

