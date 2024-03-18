package org.example.ecom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.ecom.Entity.ForProducts.Product;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @Id
    @SequenceGenerator(name = "order_item_sequence",sequenceName ="order_item_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_item_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "orders")
    private Orders orders;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "product",nullable = false)
    private Product product;
    private String size;
    private Integer quantity;
    private Double price;
    private Double dicountedPrice;
    private Long userId;
    private LocalDateTime deliveryDate;

    public OrderItem(Orders orders, Product product, String size, Integer quantity, Double price, Double dicountedPrice, Long userId) {
        this.orders = orders;
        this.product = product;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.dicountedPrice = dicountedPrice;
        this.userId = userId;
    }
}
