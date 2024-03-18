package org.example.ecom.Entity.ForUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.example.ecom.Entity.Order;
import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Entity.Rating;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "user_sequence",sequenceName ="user_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String role;
    private String mobile;
    @OneToMany(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Address> address;

    @OneToMany(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<PaymentInformation>paymentInformation;

    @OneToMany(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Rating>ratings;

    private LocalDateTime date;
    @OneToMany(mappedBy = "user",orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Orders> orders;
    @OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String firstName, String lastName, String password, String email, String mobile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = "USER";
        this.mobile = mobile;
    }
}
