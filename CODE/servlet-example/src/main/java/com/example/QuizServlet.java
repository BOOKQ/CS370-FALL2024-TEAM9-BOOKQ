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
            // Log incoming request headers
            System.out.println("Received POST request to /quiz");

            // Log the request method and headers for debugging
            System.out.println("Request Method: " + req.getMethod());
            System.out.println("Request Headers: " + req.getHeaderNames());

            // Parse quiz answers from request body
            BufferedReader reader = req.getReader();
            Map<String, String> answers = objectMapper.readValue(reader, Map.class);

            // Log the received answers
            System.out.println("Quiz answers received: " + answers);

            // Validate answers (check for missing answers)
            if (answers.isEmpty()) {
                System.err.println("No answers received in the request.");
                throw new IOException("No answers received.");
            }

            // Calculate score
            int score = 0;
            for (String answer : answers.values()) {
                if ("yes".equalsIgnoreCase(answer)) {
                    score++;
                }
            }

            // Log calculated score
            System.out.println("Calculated score: " + score);

            // Determine genre based on the score
            String recommendedGenre = getRecommendedGenre(score);

            // Log the recommended genre
            System.out.println("Recommended genre: " + recommendedGenre);

            // Fetch books from the database based on the genre
            List<Map<String, String>> recommendations = fetchBooksByGenre(recommendedGenre);

            // Log the fetched book recommendations
            System.out.println("Fetched book recommendations: " + recommendations);

            // Set session attribute for recommended books
            req.getSession().setAttribute("recommendedBooks", recommendations);

            // Response payload
            Map<String, Object> response = new HashMap<>();
            response.put("score", score);
            response.put("message", getPersonalityMessage(score));
            response.put("recommendedGenre", recommendedGenre);
            response.put("books", recommendations);

            // Send response
            resp.getWriter().write(objectMapper.writeValueAsString(response));
        } catch (IOException e) {
            // Log IO exceptions such as invalid JSON or missing answers
            System.err.println("Error during quiz submission: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid quiz submission. Please check the format of your answers.\"}");
        } catch (SQLException e) {
            // Log SQL exceptions such as database connectivity issues
            System.err.println("Error querying database: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred while fetching book recommendations.\"}");
        } catch (Exception e) {
            // Catch all other exceptions and log them
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An unexpected error occurred. Please try again later.\"}");
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
        if (score >= 9) {
            return "Fantasy";
        } 
        else if (score >= 8) {
            return "Post-apocalyptic";
        } 
        else if (score >= 7) {
            return "Adventure";
        } 
        else if (score >= 6) {
            return "Dystopian";
        } 
        else if (score == 5) {
            return "Non-Fiction";
        } 
        else if (score == 4) {
            return "Biography";
        } 
        else if (score == 3) {
            return "Thriller";
        } 
        else if (score == 2) {
            return "Romance";
        } 
        else if (score == 1) {
            return "Historical Fiction";
        } 
        else {
            return "Self-help";
        }
    }

    private List<Map<String, String>> fetchBooksByGenre(String genre) throws SQLException {
        List<Map<String, String>> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM books WHERE Genre = ?")) {
            statement.setString(1, genre);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> book = new HashMap<>();
                    book.put("BookID", String.valueOf(rs.getInt("BookID"))); // Add BookID from the ResultSet
                    book.put("Bookname", rs.getString("Bookname"));
                    book.put("Author", rs.getString("Author"));
                    book.put("ISBN_ID", rs.getString("ISBN_ID"));
                    book.put("Genre", rs.getString("Genre"));
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
