package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create a Jetty server on port 8080
        Server server = new Server(8080);

        // Set up a ServletContextHandler for servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Add the LoginServlet
        ServletHolder loginHolder = new ServletHolder(new LoginServlet());
        context.addServlet(loginHolder, "/login");

        // Add a default redirect servlet for root path
        ServletHolder defaultHolder = new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                resp.sendRedirect("login.html");
            }
        });
        context.addServlet(defaultHolder, "/");

        // Add the SignServlet
        ServletHolder signServletHolder = new ServletHolder(new SignServlet());
        context.addServlet(signServletHolder, "/signup");

        ServletHolder UserServletHolder = new ServletHolder(new UserBookRatingServlet());
        context.addServlet(signServletHolder, "/recommendations");

        // Add the QuizServlet
        ServletHolder quizServletHolder = new ServletHolder(new QuizServlet());
        context.addServlet(quizServletHolder, "/quiz");

        // Set up a ResourceHandler for static resources
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("src/main/webapp");

        // Combine handlers (ensure the ServletContextHandler comes first)
        HandlerList handlers = new HandlerList();
        handlers.addHandler(context); // ServletContextHandler first
        handlers.addHandler(resourceHandler); // ResourceHandler second

        // Set the handlers on the server
        server.setHandler(handlers);

        // Start the server
        System.out.println("Starting Jetty server...");
        server.start();
        System.out.println("Jetty server started on port 8080");
        server.join();
    }
}
