package org.example.ecom.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetails {
    private String  paymentMethod;
    private String status;
    private String razorpayId;
    private String razorpayPaymentSignature;
    private String razorpayPaymentId;

    public PaymentDetails(String status) {
        this.status = status;
    }
}
