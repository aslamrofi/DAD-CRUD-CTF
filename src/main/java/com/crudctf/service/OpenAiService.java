package com.crudctf.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    // This grabs the CRUD_OPENAI_API key you set in Railway (or application.properties)
    @Value("${openai.api.key}")
    private String apiKey;

    public String generateHint(String challengeTitle, String description) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Here we give the AI its strict personality and instructions
        String systemPrompt = "You are an underground cyber-intelligence AI. The user is solving a CTF challenge. Suggest what the answer could be, and explain it just a little bit. Keep it concise, helpful, and slightly mysterious. Do not give the exact final flag.";
        String userPrompt = "Target: " + challengeTitle + "\nIntel: " + description;

        // Build the JSON payload for OpenAI
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo"); // Fast, reliable, and cheap for text tasks
        request.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        request.put("max_tokens", 150); // Keeps the AI from rambling

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            // Send the request and parse the response
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "[SYSTEM ERROR]: Uplink to AI mainframe failed. The network is dark.";
        }
    }
}