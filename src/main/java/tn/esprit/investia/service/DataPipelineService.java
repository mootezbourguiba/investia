package tn.esprit.investia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.investia.model.CryptoCurrency;
import tn.esprit.investia.repository.CryptoCurrencyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataPipelineService {

    private final CoinGeckoService coinGeckoService; // Assure-toi que CoinGeckoService est correctement importé
    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    public void fetchAndStoreTopCryptocurrencies(int limit) {
        List<CryptoCurrency> cryptoList = coinGeckoService.getTopCryptocurrencies(limit);

        for (CryptoCurrency crypto : cryptoList) {
            if (!cryptoCurrencyRepository.existsByName(crypto.getName())) {
                cryptoCurrencyRepository.save(crypto);
            }
        }
    }
}