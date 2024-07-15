package entity;

import entity.enums.Attributes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withSellHistoryGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "phoneOrders", subgraph = "phoneOrders-subgraph"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "phoneOrders-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("user")
                        }
                )
        }
)

@FetchProfile(name = "withSellHistory", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = ItemsEntity.class, association = "phoneOrders", mode = FetchMode.JOIN
        )
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"model"})
@ToString(exclude = {"phoneOrders", "itemSalesInformation"})
@Entity(name = "items")
@Table(schema = "market")
@DynamicUpdate
@Audited
@AuditTable(value = "items_AUD", schema = "history", catalog = "market_repository")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ItemsEntity implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Lob
    @Column(name = "image")
    private String image;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private ItemSalesInformationEntity itemSalesInformation;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    // При вызове session.persist(ItemEntity) также сохранятся все связанные sellHistoryEntity
    private List<SellHistoryEntity> phoneOrders = new ArrayList<>();

    public void addPhoneOrder(SellHistoryEntity phoneOrder) {
        phoneOrders.add(phoneOrder);
        phoneOrder.setItemId(this);
        phoneOrder.setPrice(this.getItemSalesInformation().getPrice());
    }

    public void removePhoneOrder(SellHistoryEntity itemOrder) {
        phoneOrders.remove(itemOrder);
        itemOrder.setItemId(null);
    }

}
