package entity;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "items")
public class ItemsEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;
    private String model;
    private String brand;
    private String attributes;
    private Double price;
    private String currency;
    private Integer quantity;
}
