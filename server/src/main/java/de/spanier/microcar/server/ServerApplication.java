package de.spanier.microcar.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "de.spanier.microcar.controller"} )
public class ServerApplication {

	/**
	 * Start des Servers
	 * @param args
	 */
	public static void main(String[] args) {

		SpringApplication.run(ServerApplication.class, args);
	}
}

