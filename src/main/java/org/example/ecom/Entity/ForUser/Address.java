package org.example.ecom.Entity.ForUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Id
    @SequenceGenerator(name = "address_sequence",sequenceName ="address_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "address_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String pincode;
    private String mobile;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user",nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
}
