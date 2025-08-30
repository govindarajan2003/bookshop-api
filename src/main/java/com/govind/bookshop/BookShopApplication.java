package com.govind.bookshop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * Application bootstrap.
 *
 * <p>Sets the default JVM timezone to {@code Asia/Kolkata} to make date/time
 * handling predictable across environments. Adjust to your deployment region
 * or consider using UTC and formatting at the edges.</p>
 */
@Slf4j
@SpringBootApplication
public class BookShopApplication {

	public static void main(String[] args) {
		// Ensure consistent time behavior across the app.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

		SpringApplication.run(BookShopApplication.class, args);
	}
}