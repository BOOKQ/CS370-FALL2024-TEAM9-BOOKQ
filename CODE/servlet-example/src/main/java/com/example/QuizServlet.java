package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/quiz") // Maps the servlet to /quiz
public class QuizServlet extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set the response content type to application/json
        resp.setContentType("application/json");

        try {
            // Parse the incoming JSON data from the request body
            BufferedReader reader = req.getReader();
            Map<String, String> answers = objectMapper.readValue(reader, Map.class);

            // Calculate the score based on answers
            int score = 0;
            for (String answer : answers.values()) {
                if ("yes".equalsIgnoreCase(answer)) {
                    score++;
                }
            }

            // Generate a personalized message based on score
            String message = getPersonalityMessage(score);
            String recommendedGenre = getRecommendedGenre(score);

            // Send the response back as JSON
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of(
                "score", score,
                "message", message,
                "recommendedGenre", recommendedGenre
            )));
        } catch (Exception e) {
            // Return an error if something goes wrong
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"There was an error processing your quiz submission.\"}");
        }
    }

    // Get a message based on the score
    private String getPersonalityMessage(int score) {
        if (score >= 8) {
            return "You are adventurous, energetic, and love taking risks!";
        } else if (score >= 5) {
            return "You are a balanced individual with a love for discovery and introspection.";
        } else {
            return "You tend to be introspective, reflective, and appreciate stability.";
        }
    }

    // Recommend a genre based on the score
    private String getRecommendedGenre(int score) {
        if (score >= 8) {
            return "Adventure / Action";
        } else if (score >= 5) {
            return "Mystery / Thriller";
        } else {
            return "Drama / Literature";
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Sample quiz questions
        String[] quizQuestions = {
            "Do you enjoy social gatherings?",
            "Do you prefer working alone?",
            "Do you often rely on your intuition?",
            "Are you more focused on details than the big picture?",
            "Do you prefer structured routines over flexibility?",
            "Do you find it easy to express your emotions?",
            "Do you often make decisions based on logic rather than feelings?",
            "Do you feel energized in busy environments?",
            "Do you adapt quickly to new situations?",
            "Do you often prioritize others' needs over your own?"
        };

        // Convert the quiz questions to JSON and send as response
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(Map.of("quiz", quizQuestions)));
    }
}
