package dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginUserDto {

	private String email;
	private String password;
}
