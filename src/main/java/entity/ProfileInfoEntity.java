package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profile_info", schema = "market", catalog = "market_repository")
public class ProfileInfoEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private PersonalAccountEntity personalAccount;
    @Basic
    @Column(name = "language")
    private String language;
    @Basic
    @Column(name = "special_info")
    private String specialInfo;

    public void setPersonalAccount(PersonalAccountEntity account) {
        this.personalAccount = account;
        account.setProfileInfo(this);
    }
}
