package tn.esprit.investia;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Activer le profil "test" pour les tests
class InvestiaApplicationTests {

	@Test
	void contextLoads() {
		// Teste que le contexte d'application se charge correctement
	}
}