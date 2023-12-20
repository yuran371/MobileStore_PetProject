package entity;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PersonalAccountEntity {

	Long accountId;
	String email;
	String password;
	String name;
	String surname;
	LocalDate birthday;
	Country country;
	String city;
	String address;
	String phoneNumber;
	Gender gender;
}
