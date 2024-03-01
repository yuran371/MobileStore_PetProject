package entity;

import entity.enums.CurrencyEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "item_sales_information")
@Table(schema = "market")
@Audited
@AuditTable(value = "item_sales_information_AUD", schema = "history", catalog = "market_repository")
public class ItemSalesInformationEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "item_id")
//    private ItemsEntity item;

    @Column(name = "price")
    private Double price;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;

    @Column(name = "quantity")
    private Integer quantity;

}
