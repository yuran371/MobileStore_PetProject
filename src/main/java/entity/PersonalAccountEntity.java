package entity;

import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = "personal_account_AUD", schema = "history", catalog = "market_repository")
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
    private CountryEnum countryEnum;
    private String city;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum;
    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<SellHistoryEntity> phonePurchases = new ArrayList<>();
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_payment_options", joinColumns = @JoinColumn(name = "account_id"))
    private List<UserPaymentOptions> paymentOptions = new ArrayList<>();

    public void addPurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.add(phonePurchase);
        phonePurchase.setUser(this);
    }
    public void removePurchase(SellHistoryEntity phonePurchase) {
        phonePurchases.remove(phonePurchase);
        phonePurchase.setUser(null);
    }

}
