package dk.erst.cm.api.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class Basket {

	@Id
	private String id;
	private Instant createTime;
	private int version;
	private int orderCount;
	private int lineCount;

}
