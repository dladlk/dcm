package dk.erst.cm;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import dk.erst.cm.api.order.OrderProducerService.OrderDefaultConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private IntegrationProperties integration;
    
    private OrderDefaultConfig order;

    @Getter
    @Setter
    @ToString
    public static class IntegrationProperties {
        private String inboxCatalogue;
        private String inboxOrderResponse;
        private String outboxOrder;
    }

}
