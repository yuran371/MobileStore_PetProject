package entity;

import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"phonePurchases", "profileInfo"})
@Builder
@Entity
@Table(name = "personal_account", schema = "market", indexes = {
        @Index(name = "emailIndex", columnList = "email", unique = true)
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class PersonalAccountEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @ColumnTransformer(read = "pgp_sym_decrypt(" +
            "password::bytea, " +
            "current_setting('encrypt.key')" +
            ")", write = "pgp_sym_encrypt(" +
            "?, " +
            "current_setting('encrypt.key')" +
            ") ")
    @Column(columnDefinition = "bytea")
    private String password;
    private String name;
    private String surname;
    private String image;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private CountryEnum country;
    private String city;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SellHistoryEntity> phonePurchases = new ArrayList<>();
    public void addPurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.add(phonePurchase);
        phonePurchase.setUser(this);
    }
    public void removePurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.remove(phonePurchase);
        phonePurchase.setUser(null);
    }
    @OneToOne(mappedBy = "personalAccount")
    private ProfileInfoEntity profileInfo;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SellHistoryEntity> orders = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_payment_options", joinColumns = @JoinColumn(name = "account_id"))
    private List<UserPaymentOptions> paymentOptions = new ArrayList<>();

}
