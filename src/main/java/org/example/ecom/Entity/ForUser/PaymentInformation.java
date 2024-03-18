package org.example.ecom.Entity.ForUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInformation {
    @Id
    @SequenceGenerator(name = "payment_sequence",sequenceName ="payment_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "payment_sequence")
    private Long id;
    private String cardholderName;
    private  String cardNumber;
    private YearMonth expirationDate;
    private String cvv;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
}
