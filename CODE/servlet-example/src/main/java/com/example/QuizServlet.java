package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root"; // Username: root
    private static final String DB_PASSWORD = ""; // No password

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            // Parse quiz answers from request body
            BufferedReader reader = req.getReader();
            Map<String, String> answers = objectMapper.readValue(reader, Map.class);

            // Calculate score
            int score = 0;
            for (String answer : answers.values()) {
                if ("yes".equalsIgnoreCase(answer)) {
                    score++;
                }
            }

            // Determine genre based on the score
            String recommendedGenre = getRecommendedGenre(score);

            // Log the recommended genre for debugging
            System.out.println("Recommended Genre: " + recommendedGenre);

            // Fetch books from the database based on the genre
            List<Map<String, String>> recommendations = fetchBooksByGenre(recommendedGenre);

            // Log the recommendations for debugging
            System.out.println("Books found for genre " + recommendedGenre + ": " + recommendations);

            // Response payload
            Map<String, Object> response = new HashMap<>();
            response.put("score", score);
            response.put("message", getPersonalityMessage(score));
            response.put("recommendedGenre", recommendedGenre);
            response.put("books", recommendations);

            // Set session attribute for recommended books
            req.getSession().setAttribute("recommendedBooks", recommendations);

            // Send response
            resp.getWriter().write(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred while processing your quiz submission.\"}");
            e.printStackTrace();  // Log the error stack trace for debugging
        }
    }

    private String getPersonalityMessage(int score) {
        if (score >= 8) {
            return "You are adventurous, energetic, and love taking risks!";
        } else if (score >= 5) {
            return "You are a balanced individual with a love for discovery and introspection.";
        } else {
            return "You tend to be introspective, reflective, and appreciate stability.";
        }
    }

    private String getRecommendedGenre(int score) {
        if (score >= 8) {
            return "Fantasy";  // Change to match database genres
        } else if (score >= 5) {
            return "Mystery";  // Change to match database genres
        } else {
            return "Romance";  // Change to match database genres
        }
    }

    private List<Map<String, String>> fetchBooksByGenre(String genre) throws SQLException {
        List<Map<String, String>> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM books WHERE Genre = ?")) {
            statement.setString(1, genre);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> book = new HashMap<>();
                    book.put("Bookname", rs.getString("Bookname"));
                    book.put("Author", rs.getString("Author"));
                    book.put("ISBN_ID", rs.getString("ISBN_ID"));
                    book.put("PageLength", rs.getString("PageLength"));
                    books.add(book);
                }
            }
        }

        return books;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                "Do you enjoy exploring new ideas and perspectives?"
        };

        Map<String, Object> response = new HashMap<>();
        response.put("quiz", quizQuestions);

        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
