package tn.esprit.investia.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import tn.esprit.investia.model.CryptoCurrency;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CoinGeckoServiceTest {

     // Remplace SpyBean par MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CoinGeckoService coinGeckoService;

    @Test
    void testCacheBehavior() {
        List<CryptoCurrency> mockResponse = Collections.singletonList(new CryptoCurrency());

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        List<CryptoCurrency> result1 = coinGeckoService.getTopCryptocurrencies(10);
        assertNotNull(result1);

        List<CryptoCurrency> result2 = coinGeckoService.getTopCryptocurrencies(10);
        assertNotNull(result2);

        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        );
    }
}