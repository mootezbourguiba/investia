package tn.esprit.investia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.investia.model.CryptoCurrency;
import tn.esprit.investia.model.CryptoPrice;
import tn.esprit.investia.repository.CryptoPriceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CryptoPriceService {

    private final CryptoPriceRepository cryptoPriceRepository;
    private final CoinGeckoService coinGeckoService;

    @Autowired
    public CryptoPriceService(CryptoPriceRepository cryptoPriceRepository, CoinGeckoService coinGeckoService) {
        this.cryptoPriceRepository = cryptoPriceRepository;
        this.coinGeckoService = coinGeckoService;
    }

    public void saveCryptoPrice(String cryptoSymbol, String currency, Double price) {
        CryptoPrice cryptoPrice = new CryptoPrice();
        cryptoPrice.setCryptoSymbol(cryptoSymbol);
        cryptoPrice.setCurrency(currency);
        cryptoPrice.setPrice(price);

        cryptoPrice.setId(generateId(cryptoSymbol, currency));
        cryptoPrice.setTimestamp(LocalDateTime.now());

        cryptoPriceRepository.save(cryptoPrice);
        System.out.println("CryptoPrice enregistré : " + cryptoPrice.getId());
    }

    @Scheduled(fixedRate = 60000) // Exécute toutes les minutes (60000 millisecondes)
    public void updateCryptoPrices() {
        List<CryptoCurrency> cryptocurrencies = coinGeckoService.getTopCryptocurrencies(10); // Récupère les 10 premières cryptomonnaies
        for (CryptoCurrency crypto : cryptocurrencies) {
            Double price = crypto.getCurrentPrice(); // Récupère le prix depuis l'objet CryptoCurrency
            if (price != null) {
                saveCryptoPrice(crypto.getSymbol(), "USD", price);
            } else {
                System.err.println("Impossible de récupérer le prix pour " + crypto.getSymbol());
            }
        }
    }

    private String generateId(String cryptoSymbol, String currency) {
        return cryptoSymbol + "-" + currency + "-" + LocalDateTime.now();
    }
}