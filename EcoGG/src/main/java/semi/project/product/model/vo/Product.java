package semi.project.product.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class Product {
	private Long productNo;
	private Long categoryNo;
	private String productName;
	private String productDetail;
	private String productUrl;
	private String productImage;
	private Date regDate;
	private String productStatus;
}
