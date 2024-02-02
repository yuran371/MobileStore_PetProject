package entity;

import entity.enums.CountryEnum;
import entity.enums.DiscountEnum;
import entity.enums.GenderEnum;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("premium")
public class PremiumUserEntity extends PersonalAccountEntity {

    @Enumerated(EnumType.STRING)
    private DiscountEnum discount;

    @Builder(builderMethodName = "premiumBuilder")
    public PremiumUserEntity(Long id, String email, String password, String name, String surname, String image,
                             LocalDate birthday, CountryEnum country, String city, String address, String phoneNumber,
                             GenderEnum genderEnum, List<SellHistoryEntity> phonePurchases, ProfileInfoEntity profileInfo,
                             List<SellHistoryEntity> orders, List<UserPaymentOptions> paymentOptions,
                             DiscountEnum discountEnum) {
        super(id, email, password, name, surname, image, birthday, country, city, address, phoneNumber, genderEnum,
              phonePurchases, profileInfo, orders, paymentOptions);
        this.discount = discountEnum;
    }

    public PremiumUserEntity(@NonNull PersonalAccountEntity defaultAccount, DiscountEnum discountEnum) {
        super(defaultAccount.getId(),
              defaultAccount.getEmail(),
              defaultAccount.getPassword(),
              defaultAccount.getName(),
              defaultAccount.getSurname(),
              defaultAccount.getImage(),
              defaultAccount.getBirthday(),
              defaultAccount.getCountry(),
              defaultAccount.getCity(),
              defaultAccount.getAddress(),
              defaultAccount.getPhoneNumber(),
              defaultAccount.getGenderEnum(),
              defaultAccount.getPhonePurchases(),
              defaultAccount.getProfileInfo(),
              defaultAccount.getOrders(),
              defaultAccount.getPaymentOptions());
        this.discount = discountEnum;
    }

    public static PremiumUserEntity of(@NonNull PersonalAccountEntity defaultAccount, DiscountEnum discountEnum) {
        return new PremiumUserEntity(defaultAccount, discountEnum);
    }
}