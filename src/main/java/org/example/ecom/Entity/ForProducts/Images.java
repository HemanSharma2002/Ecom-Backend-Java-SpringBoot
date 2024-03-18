package org.example.ecom.Entity.ForProducts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Images {
    @Id
    @SequenceGenerator(name = "image_sequence",sequenceName ="image_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "image_sequence")
    private Long id;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "product",nullable = false)
    private Product product;
}