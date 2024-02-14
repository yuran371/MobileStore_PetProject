package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"itemId", "sellDate"})
@Table(name = "sell_history", schema = "market", catalog = "market_repository")
public class SellHistoryEntity implements BaseEntity<Long>, Cloneable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sell_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")     // 1st - in this table; 2nd - in parent table
    private ItemsEntity itemId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private PersonalAccountEntity user;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "sell_date")
    private OffsetDateTime sellDate;

    @Override
    public SellHistoryEntity clone()  {
        try {
            return (SellHistoryEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}