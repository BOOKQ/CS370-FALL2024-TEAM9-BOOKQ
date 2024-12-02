package com.example;

import com.fasterxml.jackson.databind.ObjectMapper; // Add this import for ObjectMapper
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<Map<String, String>> books = (List<Map<String, String>>) req.getSession().getAttribute("recommendedBooks");
        if (books == null) {
            books = Collections.emptyList(); // Default empty list if no recommendations exist
        }
        Map<String, Object> response = Map.of("books", books);
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }
}
