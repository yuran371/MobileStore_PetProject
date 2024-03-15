package dto.personalAccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserSellHistoryDto {

    String brand;
    String model;
    String internalMemory;
    String ram;
    String color;
    String os;
    String price;
    String quantity;
    String sellDate;
}

