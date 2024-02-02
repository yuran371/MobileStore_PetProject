package entity.enums;

public enum GenderEnum {
	MALE, FEMALE;

	public static boolean isValid(String gender) {
		try {
			GenderEnum.valueOf(gender);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
