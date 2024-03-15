package dto.personalAccount;

import jakarta.servlet.http.Part;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import validator.CheckPassword;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateAccountDto {

	@NotBlank(message = "email {jakarta.validation.constraints.NotBlank.message}")
	@Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	String email;
	@NotBlank(message = "password {jakarta.validation.constraints.NotBlank.message}")
	@CheckPassword
	String password;
	@NotBlank(message = "name {jakarta.validation.constraints.NotBlank.message}")
	String name;
	@NotBlank(message = "surname {jakarta.validation.constraints.NotBlank.message}")
	String surname;
	Part image;
	@NotBlank(message = "birthday {jakarta.validation.constraints.NotBlank.message}")
	String birthday;
	@NotBlank(message = "country {jakarta.validation.constraints.NotBlank.message}")
	String country;
	@NotBlank(message = "city {jakarta.validation.constraints.NotBlank.message}")
	String city;
	@NotBlank(message = "address {jakarta.validation.constraints.NotBlank.message}")
	String address;
	@NotBlank(message = "phone number {jakarta.validation.constraints.NotBlank.message}")
	String phoneNumber;
	@NotBlank(message = "gender {jakarta.validation.constraints.NotBlank.message}")
	String gender;
}
