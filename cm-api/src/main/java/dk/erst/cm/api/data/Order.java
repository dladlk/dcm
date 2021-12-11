package dk.erst.cm.api.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class Order {

	@Id
	private String id;
	private Instant createTime;
	private int version;

	private OrderStatus status;
	private int orderIndex;
	private String supplierName;

	@Indexed
	private String orderNumber;
	private int lineCount;

	private Object document;

	private String resultFileName;

	private Instant downloadDate;
	private Instant deliveredDate;

}
