import java.time.LocalDate;
import java.time.Period;

import utlis.DateFormatter;

public class testDateRestriction {

	public static void main(String[] args) {
		System.out.println(Period.between(DateFormatter.getDate("2010-11-05"), LocalDate.now()).getYears());
	}
}
