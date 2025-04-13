package tn.esprit.investia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.ResourceAccessException;
import tn.esprit.investia.entities.CryptoCurrency;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CoinGeckoService {

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoService.class);

    @Value("${coingecko.base-url}")
    private String coingeckoBaseUrl;

    @Value("${coingecko.rate-limit-delay}")
    private int rateLimitDelay;

    private final RestTemplate restTemplate;

    public CoinGeckoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "cryptocurrencies", key = "#limit")
    public List<CryptoCurrency> getTopCryptocurrencies(int limit) {
        String url = coingeckoBaseUrl + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";
        try {
            // Rate Limiting
            Thread.sleep(rateLimitDelay);

            ResponseEntity<List<CryptoCurrency>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CryptoCurrency>>() {}
            );
            List<CryptoCurrency> cryptoCurrencies = response.getBody();
            if (cryptoCurrencies != null) {
                logger.debug("Récupération réussie des {} cryptomonnaies depuis CoinGecko.", limit);
                return cryptoCurrencies;
            } else {
                logger.warn("L'API CoinGecko a renvoyé une réponse nulle.");
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            logger.warn("Erreur HTTP {} lors de la récupération des cryptomonnaies depuis CoinGecko : {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();
        } catch (HttpServerErrorException e) {
            logger.error("Erreur HTTP {} lors de la récupération des cryptomonnaies depuis CoinGecko : {}", e.getStatusCode(), e.getMessage());
            return Collections.emptyList();
        } catch (ResourceAccessException e) {
            logger.error("Erreur de connexion lors de la récupération des cryptomonnaies depuis CoinGecko : {}", e.getMessage());
            return Collections.emptyList();
        } catch (InterruptedException e) {
            logger.warn("Le thread a été interrompu : {}", e.getMessage());
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des cryptomonnaies depuis CoinGecko : {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}