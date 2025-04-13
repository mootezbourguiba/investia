package tn.esprit.investia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate; // Importer RestTemplate
import java.util.Collections;
import java.util.Map;

@Service
public class CryptoService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);

    // Garder l'URL ici ou la mettre dans application.properties
    private final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd,eur";

    private final RestTemplate restTemplate; // Déclarer RestTemplate comme dépendance

    // Injecter RestTemplate via le constructeur
    public CryptoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // La méthode utilise maintenant le RestTemplate injecté
    public Map<String, Map<String, Double>> getCryptoPrices() {
        try {
            // Utiliser le restTemplate injecté
            Map<String, Map<String, Double>> prices = restTemplate.getForObject(COINGECKO_API_URL, Map.class);
            if (prices != null) {
                logger.debug("Prix récupérés avec succès depuis CoinGecko simple API.");
                return prices;
            } else {
                logger.warn("L'API CoinGecko simple a retourné une réponse nulle.");
                return Collections.emptyMap(); // Retourner une map vide en cas de réponse nulle
            }
        } catch (RestClientException e) {
            logger.error("Erreur lors de l'appel à l'API CoinGecko simple : {}", e.getMessage());
            // Retourner une map vide ou lever une exception personnalisée selon le besoin
            return Collections.emptyMap();
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des prix : {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
}