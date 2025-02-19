package tn.esprit.investia.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CryptoService {

    private final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd,eur";

    public Map<String, Map<String, Double>> getCryptoPrices() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(COINGECKO_API_URL, Map.class);
    }
}