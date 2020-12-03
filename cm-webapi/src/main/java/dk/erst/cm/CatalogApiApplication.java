package dk.erst.cm;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CatalogApiApplication.class);
		app.setBannerMode(Mode.OFF);
		SpringApplication.run(CatalogApiApplication.class, args);
	}

}
