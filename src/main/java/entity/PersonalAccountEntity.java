package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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


}
