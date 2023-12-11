package dto;

public record DtoItemsFilter(Long itemId, String model, String brand, String attributes, Double price, String currency,
		Integer quantity) {

}
