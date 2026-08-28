package de.eon.kidgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the Spring Boot application and its embedded web server.
 */
@SpringBootApplication
public class KidGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(KidGenApplication.class, args);
    }
}
