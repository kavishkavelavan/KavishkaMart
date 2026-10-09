package com.kavishka.kavishkamart.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kavishka.service.ai.ChatService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AI Chatbot Proxy Servlet (Spec Section 11 & Section 17).
 * Accepts user prompt and returns JSON response envelope.
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/api/v1/chat", "/api/chat"})
public class ChatServlet extends HttpServlet {

    private final ChatService chatService = new ChatService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = parseMessage(req);
        String reply = chatService.processChatRequest(req.getSession(), message);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reply", reply);

        Map<String, Object> responseEnvelope = new LinkedHashMap<>();
        responseEnvelope.put("success", true);
        responseEnvelope.put("data", data);
        responseEnvelope.put("error", null);

        resp.getWriter().write(gson.toJson(responseEnvelope));
    }

    private String parseMessage(HttpServletRequest req) throws IOException {
        String message = req.getParameter("message");
        if (message != null && !message.trim().isEmpty()) {
            return message.trim();
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        if (sb.length() > 0) {
            try {
                JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
                if (json != null && json.has("message")) {
                    return json.get("message").getAsString();
                }
            } catch (Exception e) {
                // Ignore parse exception
            }
        }
        return "";
    }
}
