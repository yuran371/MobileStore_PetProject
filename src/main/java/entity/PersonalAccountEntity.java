package entity;

import java.util.Objects;

public class PersonalAccountEntity {

	private String login;
	private String fullName;
	private String country;
	private String city;
	private String address;
	private String phoneNumber;

	public PersonalAccountEntity() {
	}

	public PersonalAccountEntity(String login, String fullName, String country, String city, String address,
			String phoneNumber) {
		this.login = login;
		this.fullName = fullName;
		this.country = country;
		this.city = city;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	@Override
	public int hashCode() {
		return Objects.hash(address, city, country, fullName, login, phoneNumber);
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
		return Objects.equals(address, other.address) && Objects.equals(city, other.city)
				&& Objects.equals(country, other.country) && Objects.equals(fullName, other.fullName)
				&& Objects.equals(login, other.login) && Objects.equals(phoneNumber, other.phoneNumber);
	}

	@Override
	public String toString() {
		return "PersonalAccountEntity [login=" + login + ", fullName=" + fullName + ", country=" + country + ", city="
				+ city + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
	}

}
