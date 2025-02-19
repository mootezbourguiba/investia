package tn.esprit.investia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InvestiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestiaApplication.class, args);
	}

	// Ajout du bean RestTemplate pour résoudre le problème de dépendance
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}