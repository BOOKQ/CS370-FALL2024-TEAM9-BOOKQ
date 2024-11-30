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
import java.util.Date;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Database connection details
            String dbUrl = "jdbc:mysql://localhost:3306/bookq";
            String dbUser = "root";
            String dbPassword = "";

            // Establish connection
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Query to fetch book data
            String sql = "SELECT book_id, title, isbn13, num_pages, publication_date, publisher_id FROM book";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Populate the list
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("isbn13"),
                        rs.getInt("num_pages"),
                        rs.getDate("publication_date"),
                        rs.getInt("publisher_id")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log the error for debugging
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Add the books list to the request and forward it to the JSP
        request.setAttribute("books", books);
        request.getRequestDispatcher("books.jsp").forward(request, response);
    }

    // Book class to hold book data
    public static class Book {
        private int bookId;
        private String title;
        private String isbn13;
        private int numPages;
        private Date publicationDate;
        private int publisherId;

        public Book(int bookId, String title, String isbn13, int numPages, Date publicationDate, int publisherId) {
            this.bookId = bookId;
            this.title = title;
            this.isbn13 = isbn13;
            this.numPages = numPages;
            this.publicationDate = publicationDate;
            this.publisherId = publisherId;
        }

        public int getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getIsbn13() { return isbn13; }
        public int getNumPages() { return numPages; }
        public Date getPublicationDate() { return publicationDate; }
        public int getPublisherId() { return publisherId; }
    }
}
