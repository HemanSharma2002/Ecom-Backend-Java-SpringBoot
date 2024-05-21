package org.example.ecom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.ForProducts.Product;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    @Id
    @SequenceGenerator(name = "rating_sequence",sequenceName ="rating_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "rating_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Product product;
    private Long productId;
    private Double rating;
    private String review;
    private String username;
    private LocalDateTime createdAt;

    public Rating(User user, Product product, Double rating, String review,String username) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.review = review;
        this.username=username;
        this.createdAt = LocalDateTime.now();
        this.productId=product.getId();
    }
}
