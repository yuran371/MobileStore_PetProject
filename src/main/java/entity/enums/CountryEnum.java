package entity.enums;

import java.util.stream.Stream;

public enum CountryEnum {
	RUSSIA, KAZAKHSTAN, UKRAINE, BELARUS;

	public static CountryEnum getValue(String country) {
		return Stream.of(CountryEnum.values()).filter(enumCountry -> enumCountry.name().equals(country)).findFirst()
				.orElse(null);
	}

	public static boolean isValid(String country) {
		return getValue(country) != null;
	}
}
