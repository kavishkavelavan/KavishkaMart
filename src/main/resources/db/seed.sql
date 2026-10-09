-- KavishkaMart Seed Data
-- Seed accounts:
-- Admin: admin@kavishkamart.com / Admin123!
-- Seller 1: seller1@kavishkamart.com / Seller123!
-- Seller 2: seller2@kavishkamart.com / Seller123!
-- Buyer 1: buyer1@kavishkamart.com / Buyer123!

INSERT INTO users (name, email, password_hash, role) VALUES
('System Administrator', 'admin@kavishkamart.com', '$2a$10$RJaJIyivhuv8fY6ZfKBRAOf8KluNv8LqdzSAFq1TyDR09EARj30q2', 'ADMIN'),
('Apex Tech Store', 'seller1@kavishkamart.com', '$2a$10$N0jPREQ0vMtVyhDpV.4FZOUrXmCH5Aby2NvopWLmzjIcN2JISF1.u', 'SELLER'),
('Urban Style Apparel', 'seller2@kavishkamart.com', '$2a$10$N0jPREQ0vMtVyhDpV.4FZOUrXmCH5Aby2NvopWLmzjIcN2JISF1.u', 'SELLER'),
('John Doe', 'buyer1@kavishkamart.com', '$2a$10$43OCPDUJlKhgp9X5zy6IK.Qoz5Xd/BwwLHI6sQjQz/4YiUexHPcXy', 'BUYER');

INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES
(2, 'Wireless Noise-Canceling Headphones', 'High-fidelity audio with active noise cancellation and 30-hour battery life.', 199.99, 45, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500'),
(2, 'Mechanical Gaming Keyboard', 'RGB backlit mechanical keyboard with tactile blue switches and aluminum frame.', 89.50, 30, 'Electronics', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500'),
(3, 'Classic Denim Jacket', 'Premium cotton vintage denim jacket with durable stitching and modern fit.', 64.99, 60, 'Fashion', 'https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=500'),
(3, 'Minimalist Leather Watch', 'Water-resistant analog wristwatch with genuine leather strap and sapphire glass.', 120.00, 20, 'Fashion', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=500'),
(2, 'Ergonomic Desk Chair', 'Breathable mesh office chair with lumbar support and adjustable armrests.', 249.00, 15, 'Home & Office', 'https://images.unsplash.com/photo-1580481072645-022f9a6d83d0?w=500'),
(2, 'Smart Fitness Tracker Watch', 'Heart rate monitor, step tracker, GPS tracking, and AMOLED touch display.', 149.99, 50, 'Electronics', 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=500'),
(2, 'Ultra HD 4K Gaming Monitor', '27-inch IPS display with 144Hz refresh rate, 1ms response time, and HDR400.', 329.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500'),
(2, 'Noise-Isolating Earbuds', 'True wireless bluetooth earbuds with IPX7 waterproofing and compact charging case.', 49.99, 80, 'Electronics', 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=500'),
(2, 'Wireless Precision Gaming Mouse', '16,000 DPI optical sensor with customizable RGB lighting and ergonomic grip.', 59.99, 40, 'Electronics', 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500'),
(3, 'Cotton Comfort Hoodie', 'Ultra-soft fleece lined pullover hoodie available in multiple color options.', 45.00, 75, 'Fashion', 'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=500'),
(3, 'Polarized UV Protection Sunglasses', 'Lightweight titanium frame sunglasses with UV400 anti-glare polarized lenses.', 35.50, 60, 'Fashion', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=500'),
(3, 'Waterproof Commuter Backpack', 'Spacious laptop bag with anti-theft hidden pockets and USB charging port.', 79.99, 35, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500'),
(3, 'Breathable Running Sneakers', 'Lightweight mesh athletic shoes with shock-absorbing cushion soles.', 110.00, 45, 'Fashion', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500'),
(2, 'Modern Dimmable LED Desk Lamp', 'Touch-control table lamp with wireless phone charging pad and color modes.', 39.99, 50, 'Home & Office', 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=500'),
(2, 'Vacuum Insulated Stainless Water Bottle', 'Double-wall thermal flask keeping drinks cold for 24 hours or hot for 12 hours.', 24.99, 100, 'Fitness', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500'),
(2, 'Non-Slip Eco Yoga & Fitness Mat', 'Extra thick high-density foam exercise mat with carrying strap.', 29.99, 65, 'Fitness', 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=500'),
(2, 'Extended Felt Desk Mat Pad', 'Large non-slip wool felt desk protector for keyboard and mouse setup.', 19.99, 90, 'Home & Office', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=500'),
(2, 'Smart Assistant Speaker', 'Voice-controlled smart home hub speaker with rich room-filling bass audio.', 89.00, 30, 'Electronics', 'https://images.unsplash.com/photo-1543512214-318c7553f230?w=500'),
(3, 'Hardcover Leather Journal Notebook', '200-page lined notebook with premium thick paper and ribbon bookmark.', 18.50, 120, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500'),
(2, 'Ergonomic Vertical Wireless Mouse', 'Scientifically designed vertical mouse reducing wrist strain for long office hours.', 42.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=500');

INSERT INTO coupons (code, discount_percent) VALUES
('WELCOME10', 10),
('KAVISHKA20', 20),
('SUPER50', 50);
