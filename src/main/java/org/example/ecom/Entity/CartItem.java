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
public class CartItem {
    @Id
    @SequenceGenerator(name = "cart_item_sequence",sequenceName ="cart_item_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cart_item_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "cart")
    private Cart cart;
    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "product",nullable = false)
    private Product product;
    private String size;
    private Integer quantity;
    private Double price;
    private Integer totalDiscount;
    private Double dicountedPrice;
}
