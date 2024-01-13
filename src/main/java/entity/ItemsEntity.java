package entity;

import java.util.Objects;

import lombok.Builder;
@Builder
public class ItemsEntity {
	private Long itemId;
	private String model;
	private String brand;
	private String attributes;
	private Double price;
	private String currency;
	private Integer quantity;

	public ItemsEntity(Long itemId, String model, String brand, String attributes, Double price,
			String currency, Integer quantity) {
		super();
		this.itemId = itemId;
		this.model = model;
		this.brand = brand;
		this.attributes = attributes;
		this.price = price;
		this.currency = currency;
		this.quantity = quantity;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attributes, brand, currency, itemId, model, price, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemsEntity other = (ItemsEntity) obj;
		return Objects.equals(attributes, other.attributes) && Objects.equals(brand, other.brand)
				&& Objects.equals(currency, other.currency) && Objects.equals(itemId, other.itemId)
				&& Objects.equals(model, other.model) && Objects.equals(price, other.price)
				&& Objects.equals(quantity, other.quantity);
	}

	@Override
	public String toString() {
		return "ItemsEntity [itemId=" + itemId + ", model=" + model + ", brand=" + brand + ", attributes=" + attributes
				+ ", price=" + price + ", currency=" + currency + ", quantity=" + quantity + "]";
	}

}
