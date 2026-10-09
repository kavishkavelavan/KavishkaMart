package com.kavishka.service.ai;

/**
 * Interface for AI Chat Providers (Spec Section 17).
 * Supports pluggable Gemini / Mock LLM engines.
 */
public interface ChatProvider {
    String getReply(String userMessage, String context);
}
