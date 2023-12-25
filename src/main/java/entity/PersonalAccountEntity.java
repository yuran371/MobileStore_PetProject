package entity;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PersonalAccountEntity {

	Long accountId;
	String email;
	String password;
	String name;
	String surname;
	String image;
	LocalDate birthday;
	Country country;
	String city;
	String address;
	String phoneNumber;
	Gender gender;

}
