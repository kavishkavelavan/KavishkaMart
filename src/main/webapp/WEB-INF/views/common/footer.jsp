    </main>
    <footer>
        <p>&copy; 2026 KavishkaMart E-Commerce Marketplace. Built for Anna University R2025 Semester 3 Project Window.</p>
    </footer>

    <!-- AI Chatbot Floating Widget (Spec Section 11 & Section 17) -->
    <div id="ai-chat-launcher" onclick="toggleAiChat()" style="position: fixed; bottom: 25px; right: 25px; width: 55px; height: 55px; border-radius: 50%; background: linear-gradient(135deg, #6366f1, #a855f7); color: white; display: flex; align-items: center; justify-content: center; cursor: pointer; box-shadow: 0 8px 24px rgba(99, 102, 241, 0.4); z-index: 99999; transition: transform 0.2s ease;">
        <i class="fa-solid fa-robot" style="font-size: 1.5rem;"></i>
    </div>

    <div id="ai-chat-modal" style="display: none; position: fixed; bottom: 90px; right: 25px; width: 360px; max-width: 90vw; height: 480px; background: rgba(15, 23, 42, 0.95); backdrop-filter: blur(16px); border: 1px solid rgba(255, 255, 255, 0.15); border-radius: 16px; box-shadow: 0 16px 40px rgba(0,0,0,0.6); z-index: 99999; flex-direction: column; overflow: hidden;">
        <div style="background: linear-gradient(135deg, #6366f1, #8b5cf6); padding: 1rem 1.25rem; color: white; display: flex; justify-content: space-between; align-items: center;">
            <div style="display: flex; align-items: center; gap: 0.6rem;">
                <i class="fa-solid fa-robot" style="font-size: 1.2rem;"></i>
                <div>
                    <strong style="display: block; font-size: 0.95rem;">KavishkaMart AI Assistant</strong>
                    <span style="font-size: 0.75rem; opacity: 0.9;">Online • Instant E-Commerce Help</span>
                </div>
            </div>
            <button onclick="toggleAiChat()" style="background: none; border: none; color: white; font-size: 1.2rem; cursor: pointer;">&times;</button>
        </div>

        <div id="ai-chat-messages" style="flex: 1; padding: 1rem; overflow-y: auto; display: flex; flex-direction: column; gap: 0.75rem; font-size: 0.88rem;">
            <div style="background: rgba(255,255,255,0.08); padding: 0.75rem 1rem; border-radius: 12px; max-width: 85%; align-self: flex-start; border: 1px solid rgba(255,255,255,0.1);">
                👋 Hi! I am your AI Shopping Assistant. Ask me anything about products, shipping, orders, or promo coupons!
            </div>
        </div>

        <!-- Quick Suggestions -->
        <div style="padding: 0.4rem 0.75rem; display: flex; gap: 0.4rem; overflow-x: auto; background: rgba(0,0,0,0.2);">
            <button onclick="sendQuickAiMsg('What shipping options do you have?')" style="background: rgba(255,255,255,0.1); border: none; color: #cbd5e1; padding: 0.25rem 0.6rem; border-radius: 12px; font-size: 0.75rem; cursor: pointer; whitespace: nowrap;">🚚 Shipping</button>
            <button onclick="sendQuickAiMsg('What promo coupons are available?')" style="background: rgba(255,255,255,0.1); border: none; color: #cbd5e1; padding: 0.25rem 0.6rem; border-radius: 12px; font-size: 0.75rem; cursor: pointer; whitespace: nowrap;">🎟️ Coupons</button>
            <button onclick="sendQuickAiMsg('How do I track my order?')" style="background: rgba(255,255,255,0.1); border: none; color: #cbd5e1; padding: 0.25rem 0.6rem; border-radius: 12px; font-size: 0.75rem; cursor: pointer; whitespace: nowrap;">📦 Tracking</button>
        </div>

        <div style="padding: 0.75rem; background: rgba(15, 23, 42, 0.9); border-top: 1px solid rgba(255, 255, 255, 0.1); display: flex; gap: 0.5rem;">
            <input type="text" id="ai-chat-input" placeholder="Type your question..." onkeypress="handleAiKeyPress(event)" style="flex: 1; background: rgba(255,255,255,0.08); border: 1px solid rgba(255,255,255,0.15); color: white; padding: 0.5rem 0.75rem; border-radius: 8px; font-size: 0.85rem; outline: none;" />
            <button onclick="sendAiMessage()" class="btn btn-primary btn-sm" style="padding: 0.5rem 0.85rem;">
                <i class="fa-solid fa-paper-plane"></i>
            </button>
        </div>
    </div>

    <script>
        function toggleAiChat() {
            const modal = document.getElementById('ai-chat-modal');
            modal.style.display = (modal.style.display === 'none' || !modal.style.display) ? 'flex' : 'none';
        }

        function handleAiKeyPress(e) {
            if (e.key === 'Enter') sendAiMessage();
        }

        function sendQuickAiMsg(text) {
            document.getElementById('ai-chat-input').value = text;
            sendAiMessage();
        }

        async function sendAiMessage() {
            const input = document.getElementById('ai-chat-input');
            const msg = input.value.trim();
            if (!msg) return;

            const container = document.getElementById('ai-chat-messages');
            
            // Add user message bubble
            const userMsgDiv = document.createElement('div');
            userMsgDiv.style.cssText = 'background: #6366f1; color: white; padding: 0.6rem 0.9rem; border-radius: 12px; max-width: 85%; align-self: flex-end;';
            userMsgDiv.textContent = msg;
            container.appendChild(userMsgDiv);

            input.value = '';
            container.scrollTop = container.scrollHeight;

            // Add loading indicator
            const loadingDiv = document.createElement('div');
            loadingDiv.style.cssText = 'background: rgba(255,255,255,0.08); color: var(--text-muted); padding: 0.5rem 0.9rem; border-radius: 12px; max-width: 85%; align-self: flex-start; font-style: italic;';
            loadingDiv.textContent = 'AI is typing...';
            container.appendChild(loadingDiv);
            container.scrollTop = container.scrollHeight;

            try {
                const resp = await fetch('${pageContext.request.contextPath}/api/v1/chat', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({message: msg})
                });
                const res = await resp.json();
                container.removeChild(loadingDiv);

                const botMsgDiv = document.createElement('div');
                botMsgDiv.style.cssText = 'background: rgba(255,255,255,0.08); color: white; padding: 0.6rem 0.9rem; border-radius: 12px; max-width: 85%; align-self: flex-start; border: 1px solid rgba(255,255,255,0.1);';
                
                const reply = (res && res.data && res.data.reply) ? res.data.reply : 'Sorry, I could not process that request.';
                botMsgDiv.innerHTML = reply.replace(/\n/g, '<br>');
                container.appendChild(botMsgDiv);
            } catch (e) {
                container.removeChild(loadingDiv);
                const errorDiv = document.createElement('div');
                errorDiv.style.cssText = 'background: rgba(239,68,68,0.2); color: #ef4444; padding: 0.6rem 0.9rem; border-radius: 12px; max-width: 85%; align-self: flex-start;';
                errorDiv.textContent = 'Failed to connect to AI Assistant.';
                container.appendChild(errorDiv);
            }
            container.scrollTop = container.scrollHeight;
        }
    </script>
</body>
</html>
