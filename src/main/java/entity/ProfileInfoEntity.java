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
public class ProfileInfoEntity implements BaseEntity<Long> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private PersonalAccountEntity personalAccount;
    @Basic
    @Column(name = "language")
    private String language;
    @Basic
    @Column(name = "special_info")
    private String specialInfo;

}
