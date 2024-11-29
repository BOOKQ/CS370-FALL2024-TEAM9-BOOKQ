package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class QuizServlet extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Parse JSON input
        BufferedReader reader = req.getReader();
        Map<String, String> answers = objectMapper.readValue(reader, Map.class);

        // Calculate the score
        int score = 0;
        for (String answer : answers.values()) {
            if ("yes".equalsIgnoreCase(answer)) {
                score++;
            }
        }

        // Prepare the response
        String message;
        if (score >= 8) {
            message = "Your personality leans towards being highly outgoing and adaptable!";
        } else if (score >= 5) {
            message = "You have a balanced personality with traits of both extroversion and introspection.";
        } else {
            message = "You seem to be introspective and prefer a more structured environment.";
        }

        // Send JSON response
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(Map.of(
            "score", score,
            "message", message
        )));
    }
}
