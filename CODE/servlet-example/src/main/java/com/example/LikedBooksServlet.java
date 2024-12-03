package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/likedBooks")
public class LikedBooksServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<LikedBook> likedBooks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // User ID - assuming user is logged in
        Integer userId = 1;  // Simulating a logged-in user with ID 1

        try {
            // Database connection 
            String dbUrl = "jdbc:mysql://localhost:3306/bookq";
            String dbUser = "root";
            String dbPassword = "";

            // Mkae connection 
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            //  to get liked books for the user
            String sql = "SELECT b.title, b.author FROM liked_books lb JOIN books b ON lb.book_id = b.id WHERE lb.user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);  // Set user ID parameter
            rs = stmt.executeQuery();

            //  list with liked books
            while (rs.next()) {
                likedBooks.add(new LikedBook(
                        rs.getString("title"),
                        rs.getString("author")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();  //
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Add liked books list to request and forward it to the JSP
        request.setAttribute("likedBooks", likedBooks);
        request.getRequestDispatcher("likedBooks.jsp").forward(request, response);
    }

    //  to hold liked book data
    public static class LikedBook {
        private String title;
        private String author;

        public LikedBook(String title, String author) {
            this.title = title;
            this.author = author; 
        }

        public String getTitle() { 
          return title; }
        public String getAuthor() { 
          return author; }
    }
