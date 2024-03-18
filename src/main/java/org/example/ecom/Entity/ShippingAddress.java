package org.example.ecom.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddress {
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String pincode;
    private String mobile;
}
