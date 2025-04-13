package tn.esprit.investia.config; // Assure-toi que le package est correct

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Indique à Spring que cette classe contient des configurations de Beans
public class AppConfig {

    @Bean // Indique que cette méthode crée un Bean géré par Spring
    public RestTemplate restTemplate() {
        // Crée et retourne une instance de RestTemplate
        // Spring va maintenant pouvoir l'injecter là où tu le demandes (@Autowired ou via constructeur)
        return new RestTemplate();
    }
}