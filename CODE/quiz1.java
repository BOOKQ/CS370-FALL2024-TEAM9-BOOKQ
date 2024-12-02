package com.example;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/submitQuiz")
public class QuizServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String favoriteGenre = request.getParameter("favorite_genre");
        String leastFavoriteGenre = request.getParameter("least_favorite_genre");
        String bookLengthPreference = request.getParameter("book_length_preference");
        int userId = Integer.parseInt(request.getParameter("user_id"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO quiz_responses (user_id, favorite_genre, least_favorite_genre, book_length_preference) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, favoriteGenre);
            stmt.setString(3, leastFavoriteGenre);
            stmt.setString(4, bookLengthPreference);

            int rowsInserted = stmt.executeUpdate();

            out.println(rowsInserted > 0 ? "Quiz responses saved!" : "Failed to save quiz responses.");

            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}
