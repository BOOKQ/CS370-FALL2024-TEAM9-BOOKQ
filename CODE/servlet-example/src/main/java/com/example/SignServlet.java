package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/signup")
public class SignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Connection to database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to signup.html
        response.sendRedirect("signup.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Ensure signup form collects the password

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create table if it doesn't exist
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

            // Prepare SQL statement for insertion
            String sql = "INSERT INTO accounts (firstname, lastname, email, username, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            stmt.setString(4, username);
            stmt.setString(5, password); // Store password directly, hash it for production

            // Execute insertion
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<script>");
                out.println("alert('Account created successfully!');");
                out.println("window.location.href = 'login.html';");
                out.println("</script>");
            } else {
                out.println("<h3>Account creation failed. Please try again.</h3>");
            }

            // Close resources
            createTableStmt.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
