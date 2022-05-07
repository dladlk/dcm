package dk.erst.cm.webapi;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.AppProperties;
import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.order.BasketService;
import dk.erst.cm.api.order.BasketService.SendBasketData;
import dk.erst.cm.api.order.BasketService.SendBasketResponse;
import dk.erst.cm.api.order.BasketService.SentBasketData;
import dk.erst.cm.api.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
public class OrderController {

    private final BasketService basketService;
    private final OrderService orderService;
    private final AppProperties appProperties;

    @Autowired
    public OrderController(BasketService basketService, AppProperties appProperties, OrderService orderService) {
        this.basketService = basketService;
        this.appProperties = appProperties;
        this.orderService = orderService;
    }

    @RequestMapping(value = "/api/basket/send")
    public SendBasketResponse basketSend(@RequestBody SendBasketData query) {
        log.info("START basketSend: query=" + query);
        SendBasketResponse res = basketService.basketSend(query, appProperties.getIntegration().getOutboxOrder(), appProperties.getOrder());
        if (res.isSuccess()) {
            log.info("END basketSend OK: " + res);
        } else {
            log.info("END basketSend Error: " + res);
        }
        return res;
    }

    @RequestMapping(value = "/api/basket/{id}")
    public ResponseEntity<SentBasketData> getBasketById(@PathVariable("id") String id) {
        Optional<SentBasketData> findById = basketService.loadSentBasketData(id);
        if (findById.isPresent()) {
            return new ResponseEntity<>(findById.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/api/basket/{id}/zip", produces = "application/zip")
    public ResponseEntity<byte[]> getBasketByIdXML(@PathVariable("id") String id) {
        Optional<SentBasketData> findById = basketService.loadSentBasketData(id);
        if (findById.isPresent()) {
            ResponseEntity.BodyBuilder resp = ResponseEntity.ok();
            resp.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"basket_" + id + ".zip\"");
            resp.contentType(MediaType.parseMediaType("application/zip"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                for (Order order : findById.get().getOrderList()) {
                    ZipEntry entry = new ZipEntry(order.getId() + ".xml");
                    zos.putNextEntry(entry);
                    ByteArrayOutputStream orderStream = new ByteArrayOutputStream();
                    orderService.saveOrderXMLToStream((OrderType) order.getDocument(), orderStream);
                    zos.write(orderStream.toByteArray());
                    zos.closeEntry();
                }
            } catch (Exception e) {
                log.error("Error creating zip file", e);
            }
            return resp.body(bos.toByteArray());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/api/order/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable("id") String id) {
        Optional<String> findById = basketService.loadSentOrderAsJSON(id);
        if (findById.isPresent()) {
            return new ResponseEntity<>(findById.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/api/order/{id}/xml", produces = "application/xml")
    public ResponseEntity<byte[]> getOrderByIdXML(@PathVariable("id") String id) {
        Optional<byte[]> findById = basketService.loadSentOrderAsXML(id);
        if (findById.isPresent()) {
            ResponseEntity.BodyBuilder resp = ResponseEntity.ok();
            resp.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"order_" + id + ".xml\"");
            resp.contentType(MediaType.parseMediaType("application/xml"));
            return resp.body(findById.get());
        }
        return ResponseEntity.notFound().build();
    }
}
