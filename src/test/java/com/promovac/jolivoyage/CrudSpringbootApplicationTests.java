package com.promovac.jolivoyage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // Charge la config test
class CrudSpringbootApplicationTests {

	@Test
	void contextLoads() {
		// Le test vérifiera que le contexte de l'application se charge correctement.
	}
}
