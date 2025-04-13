package tn.esprit.investia.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceTest {

    @Mock // Mock de la dépendance RestTemplate
    private RestTemplate mockRestTemplate;

    @InjectMocks // Instance de CryptoService avec le mock injecté
    private CryptoService cryptoService;

    // L'URL utilisée par le service
    private final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd,eur";

    @Test
    @DisplayName("getCryptoPrices: Récupère les prix avec succès")
    void testGetCryptoPrices_Success() {
        // Arrange
        // 1. Préparer la réponse simulée
        Map<String, Map<String, Double>> mockApiResponse = Map.of(
                "bitcoin", Map.of("usd", 62000.0, "eur", 58000.0),
                "ethereum", Map.of("usd", 3500.0, "eur", 3200.0)
        );

        // 2. Configurer le mock RestTemplate
        when(mockRestTemplate.getForObject(eq(COINGECKO_API_URL), eq(Map.class)))
                .thenReturn(mockApiResponse);

        // Act
        Map<String, Map<String, Double>> result = cryptoService.getCryptoPrices();

        // Assert
        assertNotNull(result, "Le résultat ne devrait pas être null");
        assertFalse(result.isEmpty(), "Le résultat ne devrait pas être vide");
        assertTrue(result.containsKey("bitcoin"), "Devrait contenir la clé 'bitcoin'");
        assertTrue(result.containsKey("ethereum"), "Devrait contenir la clé 'ethereum'");
        assertEquals(62000.0, result.get("bitcoin").get("usd"), "Le prix USD de Bitcoin est incorrect");
        assertEquals(3200.0, result.get("ethereum").get("eur"), "Le prix EUR d'Ethereum est incorrect");

        // Vérifier que getForObject a été appelé
        verify(mockRestTemplate, times(1)).getForObject(eq(COINGECKO_API_URL), eq(Map.class));
    }

    @Test
    @DisplayName("getCryptoPrices: Retourne une map vide en cas d'erreur API")
    void testGetCryptoPrices_ApiError() {
        // Arrange
        // 1. Configurer le mock pour lever une exception
        when(mockRestTemplate.getForObject(eq(COINGECKO_API_URL), eq(Map.class)))
                .thenThrow(new RestClientException("API indisponible"));

        // Act
        Map<String, Map<String, Double>> result = cryptoService.getCryptoPrices();

        // Assert
        assertNotNull(result, "Le résultat ne devrait pas être null même en cas d'erreur");
        assertTrue(result.isEmpty(), "Le résultat devrait être une map vide en cas d'erreur API");

        // Vérifier que getForObject a été appelé
        verify(mockRestTemplate, times(1)).getForObject(eq(COINGECKO_API_URL), eq(Map.class));
    }

    @Test
    @DisplayName("getCryptoPrices: Retourne une map vide si la réponse API est nulle")
    void testGetCryptoPrices_NullResponse() {
        // Arrange
        when(mockRestTemplate.getForObject(eq(COINGECKO_API_URL), eq(Map.class)))
                .thenReturn(null); // Simule une réponse nulle

        // Act
        Map<String, Map<String, Double>> result = cryptoService.getCryptoPrices();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Le résultat devrait être une map vide si la réponse est nulle");
        verify(mockRestTemplate, times(1)).getForObject(eq(COINGECKO_API_URL), eq(Map.class));
    }
}