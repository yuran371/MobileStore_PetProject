package entity;

import java.time.OffsetDateTime;
import java.util.Objects;

public class SellHistoryEntity {

	private Long sellId;
	private ItemsEntity items;
	private PersonalAccountEntity personalAccount;
	private Integer quantity;
	private OffsetDateTime sellDate;

	public SellHistoryEntity() {
	}

	public SellHistoryEntity(ItemsEntity itemId, PersonalAccountEntity login, Integer quantity,
			OffsetDateTime sellDate) {
		super();
		this.sellId = sellId;
		this.items = itemId;
		this.personalAccount = login;
		this.quantity = quantity;
		this.sellDate = sellDate;
	}

	public Long getSellId() {
		return sellId;
	}

	public void setSellId(Long sellId) {
		this.sellId = sellId;
	}

	public ItemsEntity getItems() {
		return items;
	}

	public void setItems(ItemsEntity items) {
		this.items = items;
	}

	public PersonalAccountEntity getPersonalAccount() {
		return personalAccount;
	}

	public void setPersonalAccount(PersonalAccountEntity personalAccount) {
		this.personalAccount = personalAccount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public OffsetDateTime getSellDate() {
		return sellDate;
	}

	public void setSellDate(OffsetDateTime sellDate) {
		this.sellDate = sellDate;
	}

	@Override
	public String toString() {
		return "SellHistoryEntity [sellId=" + sellId + ", items=" + items + ", personalAccount=" + personalAccount
				+ ", quantity=" + quantity + ", sellDate=" + sellDate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(items, personalAccount, quantity, sellDate, sellId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SellHistoryEntity other = (SellHistoryEntity) obj;
		return Objects.equals(items, other.items) && Objects.equals(personalAccount, other.personalAccount)
				&& Objects.equals(quantity, other.quantity) && Objects.equals(sellDate, other.sellDate)
				&& Objects.equals(sellId, other.sellId);
	}

}
