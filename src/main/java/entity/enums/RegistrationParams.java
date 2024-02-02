package entity.enums;

public enum RegistrationParams {

	EMAIL("email"), PASSWORD("password"), NAME("name"), SURNAME("surname"), IMAGE("image"), BIRTHDAY("dateOfBirth"),
	COUNTRY("country"), CITY("city"), ADDRESS("address"), PHONE_NUMBER("phoneNumber"), GENDER("gender");

	public final String label;

	private RegistrationParams(String label) {
		this.label = label;
	}
}
