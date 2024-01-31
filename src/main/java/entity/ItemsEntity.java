package entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"model", "attributes"})
@ToString(exclude = "phoneOrders")
@Entity(name = "items")
@Table(schema = "market")
public class ItemsEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "model")
    private String model;
    @Column(name = "brand")
    @Enumerated(EnumType.STRING)
    private BrandEnum brand;
    @Column(name = "attributes")
    private String attributes;
    @Column(name = "price")
    private Double price;
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    @Column(name = "quantity")
    private Integer quantity;
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "item_currency", joinColumns = @JoinColumn(name = "item_id"))
    private List<CurrencyInfo> currencyInfos = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "itemId", orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)     // При вызове session.persist(ItemEntity) также сохранятся все связанные sellHistoryEntity
    private List<SellHistoryEntity> phoneOrders = new ArrayList<>();

    public void addPhoneOrder(SellHistoryEntity phoneOrder) {
        phoneOrders.add(phoneOrder);
        phoneOrder.setItemId(this);
    }
    public void removePhoneOrder(SellHistoryEntity itemOrder) {
        phoneOrders.remove(itemOrder);
        itemOrder.setItemId(null);
    }

}
