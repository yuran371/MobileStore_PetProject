package validator;

import lombok.Value;

@Value
public class Error {

	String cause;
	String message;
}
