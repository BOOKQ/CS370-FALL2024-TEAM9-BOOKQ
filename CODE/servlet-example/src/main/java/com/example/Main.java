package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create a basic Jetty server object that will listen on port 8080.
        Server server = new Server(8080);

        // Set the context for servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Add a servlet for handling login requests
        context.addServlet(LoginServlet.class, "/login");

        // Start the server
        server.start();
        System.out.println("Server started at http://localhost:8080");
        server.join();  // Wait for server to close
    }
}

