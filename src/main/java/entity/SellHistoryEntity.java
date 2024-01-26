package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"itemId", "sellDate"})
@Builder
@Table(name = "sell_history", schema = "market", catalog = "market_repository")
public class SellHistoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sell_id")
    private Long sellId;
    @Basic
    @Column(name = "item_id")
    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private PersonalAccountEntity user;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "sell_date")
    private OffsetDateTime sellDate;
}