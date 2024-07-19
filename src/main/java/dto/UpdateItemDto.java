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
public class UpdateItemDto {

    Long id;
    String model;
    String brand;
    String color;
    String os;
    String image;
    String internalMemory;
    String ram;
    @Size(min = 1, message = "Price must be over 0")
    String price;
    String currency;
    @Size(min = 0, message = "Price must be over 0")
    String quantity;

}
