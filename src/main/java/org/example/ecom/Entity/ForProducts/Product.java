package org.example.ecom.Entity.ForProducts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.ecom.Entity.Rating;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @SequenceGenerator(name = "product_sequence",sequenceName ="product_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_sequence")
    private Long id;

    private String title;
    private String brand;
    private Integer price;
    private Integer discountedPrice;
    private Integer discountPresent;
    private String description;
    private String pattern;
    private String color;
    private Integer quantity;
    @OneToMany(mappedBy = "product",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Size> sizes;
    @OneToMany(mappedBy = "product",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Images>images;
    @OneToMany(mappedBy = "product",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Rating> ratings;
    private  Integer totalRating;
    private Double avgRating;
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "category",nullable = false)
    private Category category;

    public Product(String title, String brand, Integer price, Integer discountedPrice,
                   Integer discountPresent, String description, String color, Category category,String pattern,Integer quantity) {
        this.title = title;
        this.brand = brand;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.discountPresent = discountPresent;
        this.description = description;
        this.color = color;
        this.createdAt = LocalDateTime.now();
        this.category = category;
        this.pattern=pattern;
        this.quantity=quantity;
    }
}
