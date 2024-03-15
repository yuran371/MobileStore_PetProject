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
public class ItemsInfoDto {

    @NotBlank
    String model;
    @NotBlank
    String brand;
    @NotBlank
    String color;
    @NotBlank
    String os;
    @NotBlank
    @Size(min = 1024, max = 1024*1024, message = "Image size must should not exceed 1 MB")
    byte[] image;
    @NotBlank
    String internalMemory;
    @NotBlank
    String ram;
    @NotBlank
    @Size(min = 1, message = "Price must be over 0")
    String price;
    @NotBlank
    String currency;

}
