package com.kavishka.service.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Gemini REST API Provider (Spec Section 11 & 17).
 * Communicates with Google Gemini API when GEMINI_API_KEY environment variable is configured.
 */
public class GeminiChatProvider implements ChatProvider {

    private final String apiKey;
    private final MockChatProvider fallbackProvider = new MockChatProvider();
    private final Gson gson = new Gson();

    public GeminiChatProvider() {
        this.apiKey = System.getenv("GEMINI_API_KEY");
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return fallbackProvider.getReply(userMessage, context);
        }

        try {
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);

            String systemPrompt = "You are KavishkaMart AI Shopping Assistant. Assist users with e-commerce queries strictly regarding products, orders, shipping, coupons, and seller listings. Keep answers helpful and concise.";
            String promptText = systemPrompt + "\nUser Question: " + userMessage;

            JsonObject textObj = new JsonObject();
            textObj.addProperty("text", promptText);

            JsonObject partObj = new JsonObject();
            JsonArray partsArr = new JsonArray();
            partsArr.add(textObj);
            partObj.add("parts", partsArr);

            JsonArray contentsArr = new JsonArray();
            contentsArr.add(partObj);

            JsonObject payload = new JsonObject();
            payload.add("contents", contentsArr);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(payload).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == 200) {
                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject respJson = gson.fromJson(reader, JsonObject.class);
                    JsonArray candidates = respJson.getAsJsonArray("candidates");
                    if (candidates != null && candidates.size() > 0) {
                        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                        JsonObject content = firstCandidate.getAsJsonObject("content");
                        JsonArray parts = content.getAsJsonArray("parts");
                        if (parts != null && parts.size() > 0) {
                            return parts.get(0).getAsJsonObject().get("text").getAsString().trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log and fallback
        }
        return fallbackProvider.getReply(userMessage, context);
    }
}
