package tn.esprit.investia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.investia.service.CoinGeckoService;
import tn.esprit.investia.model.CryptoCurrency;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    private final CoinGeckoService coinGeckoService;

    @Autowired
    public CryptoController(CoinGeckoService coinGeckoService) {
        this.coinGeckoService = coinGeckoService;
    }

    @GetMapping("/top")
    public List<CryptoCurrency> getTopCryptocurrencies(@RequestParam(defaultValue = "10") int limit) {
        return coinGeckoService.getTopCryptocurrencies(limit);
    }
}