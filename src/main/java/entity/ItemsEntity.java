package entity;

import entity.enums.Attributes;
import entity.enums.CurrencyEnum;
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
public class ItemsEntity implements BaseEntity<Long> {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "brand")
    @Enumerated(EnumType.STRING)
    private Attributes.BrandEnum brand;
    @Column(name = "model")
    private String model;
    @Column(name = "internal_memory")
    @Enumerated(EnumType.STRING)
    private Attributes.InternalMemoryEnum internalMemory;
    @Column(name = "ram")
    @Enumerated(EnumType.STRING)
    private Attributes.RamEnum ram;
    @Column(name = "color")
    private String color;
    @Column(name = "os")
    @Enumerated(EnumType.STRING)
    private Attributes.OperatingSystemEnum os;
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
