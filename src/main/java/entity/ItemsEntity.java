package entity;

import java.util.Objects;

public class ItemsEntity {
	private Long itemId;
	private String model;
	private String brand;
	private String attributes;
	private Double price;
	private Integer quantity;

	public ItemsEntity(Long itemId, String model, String brand, String attributes, Double price, Integer quantity) {
		super();
		this.itemId = itemId;
		this.model = model;
		this.brand = brand;
		this.attributes = attributes;
		this.price = price;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ItemsEntity [itemId=" + itemId + ", model=" + model + ", brand=" + brand + ", attributes=" + attributes
				+ ", price=" + price + ", quantity=" + quantity + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(attributes, brand, itemId, model, price, quantity);
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
				&& Objects.equals(itemId, other.itemId) && Objects.equals(model, other.model)
				&& Objects.equals(price, other.price) && Objects.equals(quantity, other.quantity);
	}

}
