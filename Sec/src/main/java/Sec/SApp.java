package Sec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"Sec", "Core"})
public class SApp {
    public static void main(String[] args) {
        SpringApplication.run(SApp.class);
    }
}
