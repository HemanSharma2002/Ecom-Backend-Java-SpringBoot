package org.example.ecom.Entity.ForProducts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Size {
    @Id
    @SequenceGenerator(name = "size_sequence",sequenceName ="size_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "size_sequence")
    private Long id;
    private String name;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "product",nullable = false)
    private Product product;
}
