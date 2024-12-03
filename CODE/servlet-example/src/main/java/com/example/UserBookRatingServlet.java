package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/rateBook")
public class UserBookRatingServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String bookId = request.getParameter("bookId");
        String rating = request.getParameter("rating");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"User not logged in\"}");
            return;
        }

        String username = (String) session.getAttribute("username");

        // Validate parameters
        if (bookId == null || rating == null || (!rating.equals("up") && !rating.equals("down"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid request parameters\"}");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Database connection established");

            // Convert bookId and determine rating
            int bookIdInt;
            try {
                bookIdInt = Integer.parseInt(bookId);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid bookId format\"}");
                return;
            }

            int ratingValue = rating.equals("up") ? 1 : 0;

            String sql = "INSERT INTO ratings (username, book_id, rating) VALUES (?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE rating = ?";
            System.out.println("Executing SQL: " + sql);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setInt(2, bookIdInt);
                stmt.setInt(3, ratingValue);
                stmt.setInt(4, ratingValue);
                stmt.executeUpdate();
                System.out.println("Rating inserted/updated successfully for bookId: " + bookIdInt);
            }

            response.getWriter().write("{\"message\":\"Rating submitted successfully\"}");
        } catch (SQLException e) {
            e.printStackTrace(); // Log the full exception for debugging
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to submit rating. Please try again later.\"}");
        }
    }
}
