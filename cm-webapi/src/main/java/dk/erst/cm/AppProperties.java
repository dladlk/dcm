package dk.erst.cm;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private IntegrationProperties integration;

    @Getter
    @Setter
    @ToString
    public static class IntegrationProperties {
        private String inboxCatalogue;
        private String inboxOrderResponse;
        private String outboxOrder;
    }
}
