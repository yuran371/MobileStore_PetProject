package dto;

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

    String model;
    String brand;
    String color;
    String os;
    String image;
    String internalMemory;
    String ram;
    String price;
    String currency;

}
