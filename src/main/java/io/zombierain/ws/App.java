package io.zombierain.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(App.PACKAGE_TO_SCAN)
@ConfigurationPropertiesScan(App.PACKAGE_TO_SCAN)
public class App {

	public static final String PACKAGE_TO_SCAN = "io.zombierain.ws";

	public static void main(String[] args) {

		SpringApplication.run(App.class, args);
	}
}
