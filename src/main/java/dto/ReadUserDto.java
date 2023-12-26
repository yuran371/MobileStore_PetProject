package dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadUserDto {

	String email;
	String name;
	String surname;
	String image;
	LocalDate birthday;
	String country;
	String city;
	String address;
	String phoneNumber;
}
