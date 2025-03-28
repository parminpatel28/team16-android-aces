package com.munchies_backend.spring_boot;

import com.munchies_backend.spring_boot.database.DatabaseConnection;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MunchiesBackendApplication {

	public static void main(String[] args) {


		SpringApplication.run(MunchiesBackendApplication.class, args);

		DatabaseConnection db_connection = new DatabaseConnection();

		db_connection.getConnection();

	}

}
