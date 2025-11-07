package com.unimagdalena.inventory_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        name = "inventory_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "product_id")
        }
)
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // (UUID)
    @Column(nullable = false, unique = true)
    private String productId;// (String)
    private int availableQuantity; //(int)
    private BigDecimal price;// (BigDecimal) – para calcular el monto total.
}
