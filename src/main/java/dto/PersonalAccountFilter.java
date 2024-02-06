package dto;

import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalAccountFilter {

    private LocalDate birthday;
    private GenderEnum gender;
    private CountryEnum country;
}
