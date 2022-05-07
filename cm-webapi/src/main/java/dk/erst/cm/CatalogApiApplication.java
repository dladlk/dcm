package dk.erst.cm;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableScheduling
public class CatalogApiApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CatalogApiApplication.class);
		app.setBannerMode(Mode.OFF);
		SpringApplication.run(CatalogApiApplication.class, args);
	}

}
