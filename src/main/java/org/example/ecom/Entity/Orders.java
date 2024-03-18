package org.example.ecom.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.ecom.Entity.ForUser.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    @Id
    @SequenceGenerator(name = "p_order_sequence",sequenceName ="p_order_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "p_order_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "orders",fetch = FetchType.EAGER,orphanRemoval = true)
    private List<OrderItem> orderItems;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryDate;
    @Embedded
    private ShippingAddress shippingAddress;
    @Embedded
    private PaymentDetails paymentDetails;
    private Double totalPrice;
    private Double totalDiscountedPrice;
    private Integer discount;
    private String orderStatus;
    private Integer totalItem;
    private LocalDateTime createdAt;

    public Orders(User user, ShippingAddress shippingAddress, Double totalPrice, Double totalDiscountedPrice, Integer discount, Integer totalItem
    ) {
        this.user = user;
        this.orderTime = LocalDateTime.now();
        this.shippingAddress = shippingAddress;
        this.paymentDetails = new PaymentDetails("PENDING");
        this.totalPrice = totalPrice;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.discount = discount;
        this.orderStatus = "PENDING";
        this.totalItem = totalItem;
        this.createdAt = LocalDateTime.now();
    }
}
