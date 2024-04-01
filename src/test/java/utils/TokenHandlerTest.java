package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utlis.TokenHandler;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@TestInstance(PER_METHOD)
@Tag(value = "TokenHandler")
public class TokenHandlerTest {

    @Test
    void generateToken_validEmail_returnToken() {
        String generatedToken = TokenHandler.generateToken("emailForTest@email.ru");
        Assertions.assertThat(generatedToken).isNotBlank();
    }

}
