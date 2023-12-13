package entity;

import java.util.Objects;

public class PersonalAccountEntity {

	private Long accountId;
	private String email;
	private String name;
	private String surname;
	private String country;
	private String city;
	private String address;
	private String phoneNumber;

	public PersonalAccountEntity() {
	}

	public PersonalAccountEntity(Long accountId, String email, String name, String surname, String country, String city,
			String address, String phoneNumber) {
		this.accountId = accountId;
		this.email = email;
		this.setName(name);
		this.setSurname(surname);
		this.country = country;
		this.city = city;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public PersonalAccountEntity(String email, String name, String surname, String country, String city, String address,
			String phoneNumber) {
		this.email = email;
		this.setName(name);
		this.setSurname(surname);
		this.country = country;
		this.city = city;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, address, city, country, email, name, phoneNumber, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonalAccountEntity other = (PersonalAccountEntity) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(address, other.address)
				&& Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(phoneNumber, other.phoneNumber) && Objects.equals(surname, other.surname);
	}

	@Override
	public String toString() {
		return "PersonalAccountEntity [accountId=" + accountId + ", login=" + email + ", name=" + name + ", surname="
				+ surname + ", country=" + country + ", city=" + city + ", address=" + address + ", phoneNumber="
				+ phoneNumber + "]";
	}

}
