package dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateAccountDto {
	
	String email;
	String password;
	String name;
	String surname;
	String birthday;
	String country;
	String city;
	String address;
	String phoneNumber;
	String gender;
	
}
