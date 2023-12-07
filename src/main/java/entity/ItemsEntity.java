package entity;

public class ItemsEntity {
	private Long itemId;
	private String model;
	private String brand;
	private String attributes;
	private Long price;
	private Integer quantity;
	public ItemsEntity(Long itemId, String model, String brand, String attributes, Long price, Integer quantity) {
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
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
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
	
}
