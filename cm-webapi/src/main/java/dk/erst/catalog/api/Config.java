package dk.erst.catalog.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("config")
@Data
public class Config {

	private String fileStorageRoot;
	private String xsdRoot;

}
