package tn.esprit.investia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod; // Import ajouté
import tn.esprit.investia.model.CryptoCurrency;
import java.util.List;

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
        ResponseEntity<List<CryptoCurrency>> response = restTemplate.exchange(
                url,
                HttpMethod.GET, // Utilise HttpMethod ici
                null,
                new ParameterizedTypeReference<List<CryptoCurrency>>() {}
        );
        return response.getBody();
    }
}