package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Ensure the accounts table exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS accounts ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "firstname VARCHAR(50) NOT NULL,"
                    + "lastname VARCHAR(50) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL UNIQUE,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password VARCHAR(255) NOT NULL"
                    + ")";
            Statement createTableStmt = conn.createStatement();
            createTableStmt.executeUpdate(createTableSQL);

            //ensures the book table is created
            String createbookSQL = "CREATE TABLE IF NOT EXISTS books ("
                + "BookID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "Bookname VARCHAR(255) NOT NULL,"
                + "Author VARCHAR(255) NOT NULL,"
                + "ISBN_ID VARCHAR(13) NOT NULL UNIQUE,"
                + "Genre VARCHAR(100) DEFAULT NULL,"
                + "PageLength INT DEFAULT NULL"
                + ")";
            Statement createbookStmt = conn.createStatement();
            createbookStmt.executeUpdate(createbookSQL);
            
            //ensures the recomme table is created
            String createRecommendationSQL = "CREATE TABLE IF NOT EXISTS recommendations ("
                + "book_title VARCHAR(255) NOT NULL,"
                + "author VARCHAR(255) NOT NULL,"
                + "genre VARCHAR(255) NOT NULL,"
                + "ISBN VARCHAR(20) NOT NULL PRIMARY KEY"
                + ")";
            Statement createRecommendationStmt = conn.createStatement();
            createRecommendationStmt.executeUpdate(createRecommendationSQL);

            


            // Prepare SQL statement to check login credentials
            String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Store username in session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                // Redirect to home page on successful login
                response.sendRedirect("home.html");
            } else {
                // Show error message
                out.println("<script>");
                out.println("alert('Invalid credentials. Please try again.');");
                out.println("</script>");
                request.getRequestDispatcher("login.html").include(request, response);
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
