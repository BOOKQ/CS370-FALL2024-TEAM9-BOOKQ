package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet({"/recommendations", "/rateBook", "/savedBooks", "/saveBook"})
public class RecommendationsServlet extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookq";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();

        if ("/recommendations".equals(path)) {
            // Handle recommendations retrieval
            retrieveRecommendedBooks(req, resp);
        } else if ("/savedBooks".equals(path)) {
            // Handle saved books retrieval from session
            retrieveSavedBooks(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Endpoint not found")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();

        if ("/rateBook".equals(path)) {
            handleRateBook(req, resp);
        } else if ("/saveBook".equals(path)) {
            handleSaveBook(req, resp);  // Handle saving books
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Endpoint not found")));
        }
    }

    private void retrieveRecommendedBooks(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Retrieve the recommended books from the session
        List<Map<String, String>> books = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");

        if (books == null) {
            books = new ArrayList<>(); // Default empty list if no recommendations exist
        }

        Map<String, Object> response = Map.of("books", books);
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }

    private void retrieveSavedBooks(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Retrieve saved books from the session
        List<Map<String, String>> savedBooks = (List<Map<String, String>>) req.getSession().getAttribute("savedBooks");

        if (savedBooks == null) {
            savedBooks = new ArrayList<>(); // Default empty list if no saved books exist
        }

        Map<String, Object> response = Map.of("books", savedBooks);
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }

    private void handleRateBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try (BufferedReader reader = req.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse the JSON input
            Map<String, String> requestData = new ObjectMapper().readValue(sb.toString(), Map.class);
            String id = requestData.get("id");
            String rating = requestData.get("rating");

            // Validate input
            if (id == null || id.isEmpty() || (!"1".equals(rating) && !"0".equals(rating))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Invalid input")));
                return;
            }

            // Handle thumbs up or thumbs down
            if ("1".equals(rating)) {
                saveBookToSession(req, id);  // Save book in session on thumbs up
            } else if ("0".equals(rating)) {
                removeBookFromRecommendations(req, id);  // Remove book from recommendations on thumbs down
            }

            // Respond back to client
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("message", "Rating processed successfully")));

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Database error")));
        }
    }

    private void handleSaveBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Store a book as "saved" (positive rating)
        try (BufferedReader reader = req.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Map<String, String> requestData = new ObjectMapper().readValue(sb.toString(), Map.class);
            String bookId = requestData.get("BookID");

            if (bookId == null || bookId.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Invalid BookID")));
                return;
            }

            // Check if the book is already saved
            List<Map<String, String>> savedBooks = (List<Map<String, String>>) req.getSession().getAttribute("savedBooks");

            if (savedBooks != null && savedBooks.stream().anyMatch(book -> book.get("BookID").equals(bookId))) {
                // Book already in saved list
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Book already saved")));
            } else {
                // Add the book to saved books list
                saveBookToSession(req, bookId);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("message", "Book saved successfully")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", "Failed to save book")));
        }
    }

    private void saveBookToSession(HttpServletRequest req, String bookId) throws SQLException {
        // Retrieve recommended books from session
        List<Map<String, String>> recommendedBooks = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");
        List<Map<String, String>> savedBooks = (List<Map<String, String>>) req.getSession().getAttribute("savedBooks");

        if (recommendedBooks == null) {
            recommendedBooks = new ArrayList<>(); // Default empty list if null
        }

        if (savedBooks == null) {
            savedBooks = new ArrayList<>(); // Create savedBooks list if it doesn't exist
        }

        // Check if the book is already in the savedBooks list
        for (Map<String, String> book : savedBooks) {
            if (book.get("BookID").equals(bookId)) {
                // If the book is already in savedBooks, return without adding it again
                return;
            }
        }

        // Find the book in recommended books and add it to saved books
        for (Map<String, String> book : recommendedBooks) {
            if (book.get("BookID").equals(bookId)) {
                savedBooks.add(book);  // Add the book to saved books list
                break;
            }
        }

        // Save the updated savedBooks list in session
        req.getSession().setAttribute("savedBooks", savedBooks);
    }

    private void removeBookFromRecommendations(HttpServletRequest req, String bookId) {
        // Remove book from session (or other data structure) for recommendations
        List<Map<String, String>> recommendedBooks = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");
        if (recommendedBooks != null) {
            recommendedBooks.removeIf(book -> book.get("BookID").equals(bookId));
            req.getSession().setAttribute("recommendedBooks", recommendedBooks);
        }
    }
}
