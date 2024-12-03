package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebServlet("/recommendations")
public class RecommendationsServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read the JSON data from the request body
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        // Parse the JSON data
        String jsonData = stringBuilder.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = objectMapper.readValue(jsonData, Map.class);
        List<Map<String, String>> books = (List<Map<String, String>>) data.get("books");

        // Shuffle the list and pick the first 3 books
        Collections.shuffle(books);
        List<Map<String, String>> recommendedBooks = books.subList(0, Math.min(3, books.size()));

        // Save recommended books in the session
        req.getSession().setAttribute("recommendedBooks", recommendedBooks);

        // Save these recommendations in the database
        saveRecommendationsInDatabase(req, recommendedBooks);

        // Redirect to recommendations.html
        resp.sendRedirect("/recommendations.html");
    }

    private void saveRecommendationsInDatabase(HttpServletRequest req, List<Map<String, String>> recommendedBooks) {
        String username = (String) req.getSession().getAttribute("username");
        if (username == null) {
            System.err.println("User not logged in, skipping database save.");
            return; // Exit if the user is not logged in
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            for (Map<String, String> book : recommendedBooks) {
                String bookName = book.get("Bookname");
                String author = book.get("Author");

                // Get bookId from the database
                String fetchSql = "SELECT BookID FROM books WHERE Bookname = ? AND Author = ?";
                try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                    fetchStmt.setString(1, bookName);
                    fetchStmt.setString(2, author);

                    try (ResultSet rs = fetchStmt.executeQuery()) {
                        if (rs.next()) {
                            int bookId = rs.getInt("BookID");

                            // Save the recommendation in the ratings table
                            String insertSql = "INSERT INTO ratings (username, book_id, rating) VALUES (?, ?, ?) " +
                                    "ON DUPLICATE KEY UPDATE rating = VALUES(rating)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                insertStmt.setString(1, username);
                                insertStmt.setInt(2, bookId);
                                insertStmt.setInt(3, 1); // Mark as recommended (rating = 1)
                                insertStmt.executeUpdate();
                                System.out.println("Saved recommendation for bookId: " + bookId);
                            }
                        } else {
                            System.err.println("No matching book found for: " + bookName + " by " + author);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while saving recommendations.");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Retrieve the recommended books from the session
        List<Map<String, String>> books = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");
        if (books == null) {
            books = new ArrayList<>(); // Default empty list if no recommendations exist
        }
        Map<String, Object> response = Map.of("books", books);
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }
}
