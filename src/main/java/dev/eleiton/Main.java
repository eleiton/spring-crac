package dev.eleiton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {

    private int counter = 0;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World: " + counter++;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}