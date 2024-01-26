package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"model", "attributes"})
@ToString(exclude = "phoneOrders")
@Entity(name = "items")
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
    @OneToMany(mappedBy = "itemId", orphanRemoval = true)
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
