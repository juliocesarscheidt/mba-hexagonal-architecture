package br.com.fullcycle.hexagonal.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.fullcycle.hexagonal")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
