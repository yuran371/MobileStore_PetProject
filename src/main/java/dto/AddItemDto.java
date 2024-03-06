package dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AddItemDto {

    String model;
    String brand;
    String color;
    String os;
    @Size(min = 1024, max = 1024*1024, message = "Image size must should not exceed 1 MB")
    byte[] image;
    String internalMemory;
    String ram;
    Double price;
    String currency;
    String quantity;

}
