package com.munchies_backend.spring_boot.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.load();
    private final String url;
    private final String username;
    private final String password;

    public DatabaseConnection() {

        url = dotenv.get("DB_URL");
        password = dotenv.get("DB_PASSWORD");
        username = dotenv.get("DB_USER");

    }

    public Connection getConnection() {
        try (Connection conn = DriverManager.getConnection(url, username, password); ) {

            System.out.println("✅ Successfully connected to the database!");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            throw new RuntimeException("Database connection Failed: " + e.getMessage());
        }
    }
}
