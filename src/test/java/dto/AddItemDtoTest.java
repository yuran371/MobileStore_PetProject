package dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import utlis.Validators;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AddItemDtoTest {

    @Test
    void dtoImageFromAdmin_Large_falseValidate() {
        byte[] arr = new byte[2048*1024];
        Arrays.fill(arr, (byte) 1);
        System.out.println(arr.length);
        AddItemDto build = AddItemDto.builder()
                .image(arr)
                .build();
        Validator validator = Validators.defaultFactory.getValidator();
        Set<ConstraintViolation<AddItemDto>> validate = validator.validate(build);
        validate.stream().forEach(System.out::println);
        assertThat(validate.isEmpty()).isEqualTo(false);
    }

}
