package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebServlet("/recommendations")
public class RecommendationsServlet extends HttpServlet {
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

        // Store the recommended books in the session
        req.getSession().setAttribute("recommendedBooks", recommendedBooks);

        // Redirect to recommendations.html
        resp.sendRedirect("/recommendations.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Retrieve the recommended books from the session
        List<Map<String, String>> books = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");
        if (books == null) {
            books = Collections.emptyList(); // Default empty list if no recommendations exist
        }
        Map<String, Object> response = Map.of("books", books);
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }
}
