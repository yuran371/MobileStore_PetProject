package dto.personalAccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReadUserInfoDto {

    String id;
    String email;
    String name;
    String surname;
    String birthday;
    String country;
    String city;
    String address;
    String phoneNumber;
    String gender;
    List<UserSellHistoryDto> purchases;
}
