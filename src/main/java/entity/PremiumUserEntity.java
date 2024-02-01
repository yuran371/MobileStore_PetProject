package entity;

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
    private Discount discount;

    @Builder(builderMethodName = "premiumBuilder")
    public PremiumUserEntity(Long id, String email, String password, String name, String surname, String image,
                             LocalDate birthday, Country country, String city, String address, String phoneNumber,
                             Gender gender, List<SellHistoryEntity> phonePurchases, ProfileInfoEntity profileInfo,
                             List<SellHistoryEntity> orders, List<UserPaymentOptions> paymentOptions,
                             Discount discount) {
        super(id, email, password, name, surname, image, birthday, country, city, address, phoneNumber, gender,
              phonePurchases, profileInfo, orders, paymentOptions);
        this.discount = discount;
    }

    public PremiumUserEntity(@NonNull PersonalAccountEntity defaultAccount, Discount discount) {
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
              defaultAccount.getGender(),
              defaultAccount.getPhonePurchases(),
              defaultAccount.getProfileInfo(),
              defaultAccount.getOrders(),
              defaultAccount.getPaymentOptions());
        this.discount = discount;
    }

    public static PremiumUserEntity of(@NonNull PersonalAccountEntity defaultAccount, Discount discount) {
        return new PremiumUserEntity(defaultAccount, discount);
    }
}
