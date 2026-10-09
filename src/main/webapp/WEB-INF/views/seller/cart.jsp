<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Shopping Cart — KavishkaMart" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="glass-card" style="max-width: 850px; margin: 2rem auto; padding: 2.5rem;">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
        <h2><i class="fa-solid fa-cart-shopping"></i> Shopping Cart</h2>
        <button class="btn btn-secondary btn-sm" onclick="clearCart()"><i class="fa-solid fa-trash"></i> Clear Cart</button>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" style="margin-bottom: 1rem;">
            <i class="fa-solid fa-triangle-exclamation"></i> ${errorMessage}
        </div>
    </c:if>

    <div class="table-container" style="overflow-x: auto;">
        <table class="table" style="width: 100%; text-align: left; border-collapse: collapse;">
            <thead>
                <tr style="border-bottom: 2px solid rgba(255,255,255,0.1);">
                    <th>Product</th>
                    <th>Price ($)</th>
                    <th>Quantity</th>
                    <th>Subtotal ($)</th>
                    <th style="text-align: right;">Action</th>
                </tr>
            </thead>
            <tbody id="cart-body">
                <tr><td colspan="5" style="text-align: center; padding: 1.5rem;">Loading cart items...</td></tr>
            </tbody>
            <tfoot>
                <tr style="border-top: 2px solid rgba(255,255,255,0.1); font-weight: bold; font-size: 1.1rem;">
                    <td colspan="3" style="text-align: right; padding-top: 1rem;">Total Amount:</td>
                    <td style="padding-top: 1rem;">$<span id="cart-total">0.00</span></td>
                    <td></td>
                </tr>
            </tfoot>
        </table>
    </div>

    <!-- Promo Code Section -->
    <div style="background: rgba(255,255,255,0.03); border: 1px dashed rgba(255,255,255,0.15); border-radius: 8px; padding: 1rem; margin-top: 1.5rem; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;">
        <div>
            <strong style="display: block; font-size: 0.95rem;"><i class="fa-solid fa-ticket" style="color:#f59e0b;"></i> Have a Promo Coupon?</strong>
            <span style="font-size: 0.8rem; color: var(--text-muted);">Try: <code>WELCOME10</code>, <code>KAVISHKA20</code>, or <code>SUPER50</code></span>
        </div>
        <div style="display: flex; gap: 0.5rem; align-items: center;">
            <input type="text" id="coupon-code-input" placeholder="Enter coupon code" class="form-control" style="padding: 0.4rem 0.75rem; background: rgba(15,23,42,0.8); color: white; border: 1px solid var(--border-color); border-radius: 6px; text-transform: uppercase;" />
            <button onclick="applyCoupon()" class="btn btn-secondary btn-sm"><i class="fa-solid fa-check"></i> Apply</button>
        </div>
    </div>
    <div id="coupon-status-msg" style="margin-top: 0.5rem; font-size: 0.85rem;"></div>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 2rem;">
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
            <i class="fa-solid fa-arrow-left"></i> Continue Shopping
        </a>

        <form action="${pageContext.request.contextPath}/buyer/checkout" method="POST" id="checkout-form" style="margin: 0;">
            <button type="submit" id="checkout-btn" class="btn btn-primary" style="padding: 0.75rem 2rem; font-size: 1.05rem;" disabled>
                <i class="fa-solid fa-credit-card"></i> Place Order (Checkout)
            </button>
        </form>
    </div>
</div>

<script>
    let rawTotal = 0;

    async function loadCart() {
        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/cart');
            const items = await resp.json();
            const tbody = document.getElementById('cart-body');
            const checkoutBtn = document.getElementById('checkout-btn');
            tbody.innerHTML = '';

            if (!items || items.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 2rem; color: var(--text-muted);">' +
                    '<i class="fa-solid fa-basket-shopping" style="font-size: 2rem; margin-bottom: 0.5rem; display: block;"></i>Your cart is empty.</td></tr>';
                document.getElementById('cart-total').textContent = '0.00';
                checkoutBtn.disabled = true;
                return;
            }

            rawTotal = 0;
            items.forEach(iv => {
                const p = iv.product;
                const qty = iv.quantity;
                const subtotal = p.price * qty;
                rawTotal += subtotal;

                const row = `
                    <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                        <td style="padding: 0.75rem 0.5rem;">
                            <strong>\${p.title || p.name}</strong>
                        </td>
                        <td>$\${p.price.toFixed(2)}</td>
                        <td>\${qty}</td>
                        <td>$\${subtotal.toFixed(2)}</td>
                        <td style="text-align: right;">
                            <button class="btn btn-danger btn-sm" onclick="removeItem(\${p.id})">
                                <i class="fa-solid fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                `;
                tbody.insertAdjacentHTML('beforeend', row);
            });

            document.getElementById('cart-total').textContent = rawTotal.toFixed(2);
            checkoutBtn.disabled = false;
        } catch (e) {
            console.error(e);
        }
    }

    async function applyCoupon() {
        const input = document.getElementById('coupon-code-input').value.trim();
        const msgDiv = document.getElementById('coupon-status-msg');
        if (!input) return;

        try {
            const resp = await fetch('${pageContext.request.contextPath}/api/coupon/validate?code=' + encodeURIComponent(input));
            const result = await resp.json();

            if (result.success && result.data) {
                const discount = result.data.discountPercent;
                const discountedTotal = rawTotal * (1 - (discount / 100));
                document.getElementById('cart-total').textContent = discountedTotal.toFixed(2);
                msgDiv.style.color = '#10b981';
                msgDiv.innerHTML = `<i class="fa-solid fa-circle-check"></i> Coupon <strong>\${result.data.code}</strong> applied! (\${discount}% OFF saved $\${(rawTotal - discountedTotal).toFixed(2)})`;
            } else {
                msgDiv.style.color = '#ef4444';
                msgDiv.innerHTML = `<i class="fa-solid fa-circle-xmark"></i> \${result.error ? result.error.message : 'Invalid coupon code'}`;
            }
        } catch (e) {
            msgDiv.style.color = '#ef4444';
            msgDiv.textContent = 'Failed to apply coupon.';
        }
    }

    async function removeItem(productId) {
        await fetch('${pageContext.request.contextPath}/api/cart?action=remove', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: `productId=\${productId}`
        });
        loadCart();
    }

    async function clearCart() {
        if (!confirm('Are you sure you want to clear your cart?')) return;
        await fetch('${pageContext.request.contextPath}/api/cart?action=clear', {method: 'POST'});
        loadCart();
    }

    window.addEventListener('DOMContentLoaded', loadCart);
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
