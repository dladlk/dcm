package dk.erst.cm.webapi;

import dk.erst.cm.api.item.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
public class BasketController {

    private final ProductService productService;

    @Autowired
    public BasketController(ProductService productService) {
        this.productService = productService;
    }

    @Data
    public static class Contact {
        private String personName;
        private String email;
        private String telephone;
    }

    @Data
    public static class Company {
        private String registrationName;
        private String legalIdentifier;
        private String partyIdentifier;
    }

    @Data
    public static class OrderData {
        private Company buyerCompany;
        private Contact buyerContact;
    }

    @Data
    public static class BasketData {
        private Map<String, Integer> orderLines;
    }

    @Data
    public static class SendBasketData {
        private BasketData basketData;
        private OrderData orderData;
    }

    @Data
    public static class SendBasketResponse {
        private boolean success;
    }

    @RequestMapping(value = "/api/basket/send")
    public SendBasketResponse basketSend(@RequestBody SendBasketData query) {
        log.info("Send basket with data " + query);
        SendBasketResponse r = new SendBasketResponse();
        r.success = true;
        return r;
    }

}
