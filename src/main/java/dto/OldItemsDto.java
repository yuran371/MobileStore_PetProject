package dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OldItemsDto {

	private Long itemId;
	private String model;
	private String brand;
	private String attributes;
	private Double price;
	private String currency;
	private Integer quantity;

	public String getCartParams() {
		return model + " " + brand + " " + price + " " + currency + " " + quantity + " шт";
	}
}
