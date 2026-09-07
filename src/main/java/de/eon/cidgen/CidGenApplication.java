package de.eon.cidgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the Spring Boot application and its embedded web server.
 */
@SpringBootApplication
public class CidGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CidGenApplication.class, args);
    }
}
