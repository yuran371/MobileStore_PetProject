package entity;

public enum Gender {
	MALE, FEMALE;

	public static boolean isValid(String gender) {
		try {
			Gender.valueOf(gender);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
