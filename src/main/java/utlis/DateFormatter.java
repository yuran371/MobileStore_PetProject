package utlis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateFormatter {

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	public static LocalDate getDate(String date) {
		return LocalDate.parse(date, FORMATTER);
	}

	public static boolean isValid(String date) {
		try {
			getDate(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
