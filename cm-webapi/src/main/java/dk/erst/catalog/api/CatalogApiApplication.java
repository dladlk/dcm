package dk.erst.catalog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class CatalogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApiApplication.class, args);
	}

}
