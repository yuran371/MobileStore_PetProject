package dto;

public record ItemsFindByIdDto(Long itemId, String model, String brand, String attributes, Double price, String currency,
		Integer quantity) {

}
