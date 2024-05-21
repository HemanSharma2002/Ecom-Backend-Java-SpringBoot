package org.example.ecom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @SequenceGenerator(name = "cart_sequence",sequenceName ="cart_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cart_sequence")
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "cart",fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> cartItems;

    private Double totalPrice;
    private Integer totalItem;
    private Integer totalDiscount;
    private Double totalDiscountedPrice;
    @OneToOne
    @JoinColumn(name = "selected_Address")
    private Address selectedAddress;

}
