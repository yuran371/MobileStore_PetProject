package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = "orders")
@Builder
@Entity
@Table(name = "personal_account", schema = "market", indexes = {
        @Index(name = "emailIndex", columnList = "email", unique = true)
})
public class PersonalAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
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
    private Country country;
    private String city;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SellHistoryEntity> orders = new HashSet<>();

    public void addOrder(SellHistoryEntity order) {
        orders.add(order);
        order.setUser(this);
    }
}

