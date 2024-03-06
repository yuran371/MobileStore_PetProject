package dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AddItemDtoTest {

    @Test
    void dtoImageFromAdmin_Large_falseValidate() {
        byte[] arr = new byte[20048*1024];
        Arrays.fill(arr, (byte) 1);
        System.out.println(arr.length);
        AddItemDto build = AddItemDto.builder()
                .image(arr)
                .build();
        System.out.println(build);
//        ValidatorFactory validatorFactory = Validation.byDefaultProvider()
    }

}
