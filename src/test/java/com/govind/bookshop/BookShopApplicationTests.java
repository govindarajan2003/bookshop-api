package com.govind.bookshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test to ensure the Spring ApplicationContext starts successfully.
 */
@SpringBootTest
class BookShopApplicationTests {

	@Test
	@DisplayName("Spring ApplicationContext should load without errors")
	void applicationContext_shouldLoad() {
		// If the context fails to start, this test will fail.
	}
}
