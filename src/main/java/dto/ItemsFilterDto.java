package dto;

import lombok.Value;

@Value
public class ItemsFilterDto {
	// record (Long itemId, String model, String brand, Double price, String
	// currency)
	Long itemId;
	String model;
	String brand;
	Double price;
	String currency;

	public String getCartParams() {
		return model + " " + brand + " " + price + " " + currency + " " + " шт";
	}
}
