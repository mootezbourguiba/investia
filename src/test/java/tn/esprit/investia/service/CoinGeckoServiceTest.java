package tn.esprit.investia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// Import nécessaire pour ReflectionTestUtils
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import tn.esprit.investia.entities.CryptoCurrency;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinGeckoServiceTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @InjectMocks
    private CoinGeckoService coinGeckoService;

    // Définir la valeur attendue de la base URL pour les tests
    private final String BASE_URL_FOR_TEST = "https://api.coingecko.com/api/v3";

    @BeforeEach
    void setUp() {
        // Injecter manuellement la valeur de coingeckoBaseUrl DANS l'instance testée
        // car @Value ne fonctionne pas dans ce type de test unitaire simple.
        ReflectionTestUtils.setField(coinGeckoService, // L'objet où injecter
                "coingeckoBaseUrl", // Le nom EXACT du champ dans CoinGeckoService
                BASE_URL_FOR_TEST); // La valeur à injecter

        // Optionnel : Injecter aussi le délai si nécessaire, mais moins critique pour le matching d'URL
        // ReflectionTestUtils.setField(coinGeckoService, "rateLimitDelay", 10);
    }

    @Test
    @DisplayName("getTopCryptocurrencies: Récupère les cryptos avec succès")
    void testGetTopCryptocurrencies_Success() throws InterruptedException {
        // Arrange
        int limit = 5;
        // Construire l'URL attendue AVEC la valeur injectée
        String expectedUrl = BASE_URL_FOR_TEST + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";

        CryptoCurrency btc = new CryptoCurrency();
        btc.setId("bitcoin");
        btc.setSymbol("btc");
        List<CryptoCurrency> mockCryptoList = List.of(btc);
        ResponseEntity<List<CryptoCurrency>> mockResponseEntity = ResponseEntity.ok(mockCryptoList);

        // Configurer le mock RestTemplate AVEC L'URL CORRIGÉE
        when(mockRestTemplate.exchange(
                eq(expectedUrl), // Utilise l'URL construite avec la valeur injectée
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponseEntity);

        // Act
        List<CryptoCurrency> result = coinGeckoService.getTopCryptocurrencies(limit);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty(), "La liste ne devrait pas être vide"); // <--- L'erreur d'assertion était ici aussi
        assertEquals(1, result.size());
        assertEquals("bitcoin", result.get(0).getId());

        verify(mockRestTemplate, times(1)).exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    @DisplayName("getTopCryptocurrencies: Retourne une liste vide en cas d'erreur HTTP client (4xx)")
    void testGetTopCryptocurrencies_HttpClientError() throws InterruptedException {
        // Arrange
        int limit = 5;
        String expectedUrl = BASE_URL_FOR_TEST + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";

        // Configurer le mock pour lever une HttpClientErrorException (ex: 404 Not Found)
        when(mockRestTemplate.exchange(
                eq(expectedUrl), // Utilise l'URL corrigée
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));

        // Act
        List<CryptoCurrency> result = coinGeckoService.getTopCryptocurrencies(limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "La liste devrait être vide en cas d'erreur HTTP client");
        verify(mockRestTemplate, times(1)).exchange(
                eq(expectedUrl), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class)
        );
    }

    @Test
    @DisplayName("getTopCryptocurrencies: Retourne une liste vide en cas d'erreur de connexion (ResourceAccessException)")
    void testGetTopCryptocurrencies_ConnectionError() throws InterruptedException {
        // Arrange
        int limit = 3;
        String expectedUrl = BASE_URL_FOR_TEST + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";

        when(mockRestTemplate.exchange(
                eq(expectedUrl), // Utilise l'URL corrigée
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new ResourceAccessException("Connection timed out"));

        // Act
        List<CryptoCurrency> result = coinGeckoService.getTopCryptocurrencies(limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "La liste devrait être vide en cas d'erreur de connexion");
        verify(mockRestTemplate, times(1)).exchange(eq(expectedUrl), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class));
    }

    @Test
    @DisplayName("getTopCryptocurrencies: Retourne une liste vide si la réponse de l'API est nulle")
    void testGetTopCryptocurrencies_NullResponse() throws InterruptedException {
        // Arrange
        int limit = 2;
        String expectedUrl = BASE_URL_FOR_TEST + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=" + limit + "&page=1";
        ResponseEntity<List<CryptoCurrency>> mockResponseEntity = ResponseEntity.ok(null);

        when(mockRestTemplate.exchange(
                eq(expectedUrl), // Utilise l'URL corrigée
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(mockResponseEntity);

        // Act
        List<CryptoCurrency> result = coinGeckoService.getTopCryptocurrencies(limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "La liste devrait être vide si le corps de la réponse est null");
        verify(mockRestTemplate, times(1)).exchange(eq(expectedUrl), eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class));
    }
}