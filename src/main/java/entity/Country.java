package entity;

import java.util.stream.Stream;

public enum Country {
	RUSSIA, KAZAKHSTAN, UKRAINE, BELARUS;

	public static Country getValue(String country) {
		return Stream.of(Country.values()).filter(enumCountry -> enumCountry.name().equals(country)).findFirst()
				.orElse(null);
	}

	public static boolean isValid(String country) {
		return getValue(country) == null ? false : true;
	}
}
