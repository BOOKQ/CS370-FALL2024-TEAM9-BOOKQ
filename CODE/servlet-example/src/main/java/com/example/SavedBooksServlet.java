package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/savedbooks")
public class SavedBooksServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.getWriter().write("{\"error\":\"User not logged in\"}");
            return;
        }

        List<Map<String, String>> savedBooks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT b.Bookname, b.Author FROM books b " +
                         "JOIN ratings r ON b.BookID = r.book_id " +
                         "WHERE r.username = ? AND r.rating = 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> book = new HashMap<>();
                        book.put("Bookname", rs.getString("Bookname"));
                        book.put("Author", rs.getString("Author"));
                        savedBooks.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Failed to fetch saved books\"}");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(savedBooks));
    }
}
