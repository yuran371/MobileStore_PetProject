package dto.personalAccount;

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
public class AuthUserDto {

    @NotBlank(message = "email {jakarta.validation.constraints.NotBlank.message}")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    String email;
    @NotBlank(message = "password {jakarta.validation.constraints.NotBlank.message}")
    @CheckPassword
    String password;
}
