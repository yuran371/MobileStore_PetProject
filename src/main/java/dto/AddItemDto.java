package dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    String model;
    @NotBlank
    String brand;
    @NotBlank
    String color;
    @NotBlank
    String os;
    @NotBlank
    String image;
    @NotBlank
    String internalMemory;
    @NotBlank
    String ram;
    @NotBlank
    @Size(min = 1, message = "Price must be over 0")
    String price;
    @NotBlank
    String currency;
    @NotBlank
    @Size(min = 0, message = "Price must be over 0")
    String quantity;

}
