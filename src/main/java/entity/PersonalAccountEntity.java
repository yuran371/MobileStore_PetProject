package entity;

import entity.enums.CountryEnum;
import entity.enums.DiscountEnum;
import entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import validator.CheckBirthday;
import validator.CreateUserGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withItems",
        attributeNodes = {
                @NamedAttributeNode(value = "phonePurchases", subgraph = "phonePurchases-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "phonePurchases-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("itemId")
                        }
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"phonePurchases"})
@Builder
@Entity
@Table(name = "personal_account", schema = "market", indexes = {
        @Index(name = "emailIndex", columnList = "email", unique = true)
})
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
    @CheckBirthday(groups = CreateUserGroup.class)
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private CountryEnum countryEnum;
    private String city;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SellHistoryEntity> phonePurchases = new ArrayList<>();
    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_payment_options", joinColumns = @JoinColumn(name = "account_id"))
    private List<UserPaymentOptions> paymentOptions = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DiscountEnum discountEnum = null;

    public void addPurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.add(phonePurchase);
        phonePurchase.setUser(this);
    }
    public void removePurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.remove(phonePurchase);
        phonePurchase.setUser(null);
    }

}
