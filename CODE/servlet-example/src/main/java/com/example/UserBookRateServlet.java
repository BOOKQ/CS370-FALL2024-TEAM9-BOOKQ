package com.example;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
@WebServlet("/rateBook")
public class UserBookRatingServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("bookId");
        String rating = request.getParameter("rating"); // Rating value (thumbs up or thumbs down)
        String username = (String) request.getSession().getAttribute("username"); // Get logged in user s
        if (!rating.equals("up") && !rating.equals("down")) {
            response.getWriter().write("{\"error\":\"Invalid rating value\"}");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Insert rating into ratings table (using 1 for thumbs up, 0 for thumbs down)
            String sql = "INSERT INTO ratings (username, book_id, rating) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, bookId);
                stmt.setInt(3, rating.equals("up") ? 1 : 0); // ocnvert thumbs up/down to 1/0
                stmt.executeUpdate();
            }
            response.getWriter().write("{\"message\":\"Rating submitted successfully\"}");
        } catch (SQLException e) {
            response.getWriter().write("{\"error\":\"Failed to submit rating\"}");
        }
    }
}
