package org.example.ecom.Controler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentLinkResponse {
    private String paymentLinkId;
    private String paymentLinkUrl;
    private String orderId;

}
