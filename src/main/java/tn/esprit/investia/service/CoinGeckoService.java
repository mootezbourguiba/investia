package tn.esprit.investia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import tn.esprit.investia.model.CryptoCurrency;

import java.util.List;
import java.util.Collections;

@Service
public class CoinGeckoService {

    @Value("${coingecko.base-url}")
    private String coingeckoBaseUrl;

    private final RestTemplate restTemplate;

    public CoinGeckoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "cryptocurrencies", key = "#limit")
    public List<CryptoCurrency> getTopCryptocurrencies(int limit) {
        String url = coingeckoBaseUrl + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";
        try {
            ResponseEntity<List<CryptoCurrency>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CryptoCurrency>>() {}
            );
            List<CryptoCurrency> cryptoCurrencies = response.getBody();
            if (cryptoCurrencies != null) {
                return cryptoCurrencies;
            } else {
                System.err.println("Erreur : Réponse de l'API CoinGecko est nulle");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            // Gérer l'erreur ici (par exemple, logger l'erreur, renvoyer une liste vide, etc.)
            System.err.println("Erreur lors de la récupération des cryptomonnaies depuis CoinGecko : " + e.getMessage());
            return Collections.emptyList(); // Renvoie une liste vide en cas d'erreur
        }
    }
}