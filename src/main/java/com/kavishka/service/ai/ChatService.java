package com.kavishka.service.ai;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI Chat Service Orchestration (Spec Section 11 & Section 17).
 * Enforces per-session rate limits, input validation, caching, and Provider selection.
 */
public class ChatService {

    private final ChatProvider provider;

    public ChatService() {
        String providerConfig = System.getProperty("ai.chatbot.provider", "mock");
        if ("gemini".equalsIgnoreCase(providerConfig) || System.getenv("GEMINI_API_KEY") != null) {
            this.provider = new GeminiChatProvider();
        } else {
            this.provider = new MockChatProvider();
        }
    }

    public String processChatRequest(HttpSession session, String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please type a message for AI assistance.";
        }

        String cleanedMsg = message.trim();
        if (cleanedMsg.length() > 500) {
            cleanedMsg = cleanedMsg.substring(0, 500);
        }

        // 1. Session Rate Limiting (10 messages/minute)
        Long now = System.currentTimeMillis();
        Long windowStart = (Long) session.getAttribute("chat_window_start");
        Integer messageCount = (Integer) session.getAttribute("chat_message_count");

        if (windowStart == null || (now - windowStart > 60000)) {
            session.setAttribute("chat_window_start", now);
            session.setAttribute("chat_message_count", 1);
        } else {
            if (messageCount != null && messageCount >= 10) {
                return "⚠️ Rate limit reached (Max 10 messages/minute). Please wait a moment before sending more messages.";
            }
            session.setAttribute("chat_message_count", (messageCount == null ? 1 : messageCount + 1));
        }

        // 2. In-Memory Session Cache Check
        @SuppressWarnings("unchecked")
        Map<String, String> cache = (Map<String, String>) session.getAttribute("chat_cache");
        if (cache == null) {
            cache = new ConcurrentHashMap<>();
            session.setAttribute("chat_cache", cache);
        }

        String cachedAnswer = cache.get(cleanedMsg.toLowerCase());
        if (cachedAnswer != null) {
            return cachedAnswer;
        }

        // 3. Delegate to Provider
        String reply = provider.getReply(cleanedMsg, "KavishkaMart Marketplace Context");
        cache.put(cleanedMsg.toLowerCase(), reply);
        return reply;
    }
}
