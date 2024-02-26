package exceptions;

public class EmailSendException extends Exception {

    private final static String errorMessage = "Email didnt sent on %s email";

    public EmailSendException(Throwable cause, String userEmail) {
        super(errorMessage.formatted(userEmail), cause);
    }

}
