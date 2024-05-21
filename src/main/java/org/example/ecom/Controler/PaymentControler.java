package org.example.ecom.Controler;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.mail.MessagingException;
import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Entity.PaymentDetails;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Repository.OrderRepository;
import org.example.ecom.Service.EmailService;
import org.example.ecom.Service.OrdersService;
import org.example.ecom.Service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentControler {
    @Value("${razorpay.api.key}")
    String apiKey;
    @Value("${razorpay.api.secret}")
    String apiSecret;
    private UserService userService;
    private OrdersService ordersService;
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;

    public PaymentControler(UserService userService, OrdersService ordersService, OrderRepository orderRepository) {
        this.userService = userService;
        this.ordersService = ordersService;
        this.orderRepository = orderRepository;
    }
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<String>createPaymentLink(@PathVariable Long orderId, Authentication authentication) throws OrderException, RazorpayException {
        Orders orders=ordersService.findOrdersById(orderId);
//        System.out.println(apiKey+ " "+apiSecret);
        try{
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", orders.getTotalDiscountedPrice()*100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_receipt_11");

            Order order = razorpayClient.orders.create(orderRequest);
            String id = order.get("id");

            return new ResponseEntity<>(id, HttpStatus.CREATED);

        } catch (RazorpayException e) {
            throw new RazorpayException(e.getMessage());
        }
    }
    @PostMapping("/payment/{orderId}/razorpay-info")
    public ResponseEntity<String>savePaymentInformation(@PathVariable Long orderId, @RequestBody PaymentDetails paymentDetails) throws OrderException, MessagingException {
        Orders order=ordersService.findOrdersById(orderId);
        order.getPaymentDetails().setRazorpayId(paymentDetails.getRazorpayId());
        order.getPaymentDetails().setRazorpayPaymentId(paymentDetails.getRazorpayPaymentId());
        order.getPaymentDetails().setRazorpayPaymentSignature(paymentDetails.getRazorpayPaymentSignature());
        order.getPaymentDetails().setStatus("PAID");
        order.getPaymentDetails().setPaymentMethod("ONLINE");
        order.setOrderStatus(OrderStatus.PLACED);
        orderRepository.save(order);
        emailService.sendEmailWithoutAttachment(order.getUser().getEmail(),"Dear "+order.getUser().getFirstName()+"\nYour order details are \norder id :"+order.getId()+"\nRazorpay Id :"+order.getPaymentDetails().getRazorpayId()+"\nRazorpay Payment Id :"+order.getPaymentDetails().getRazorpayPaymentId()
        +"\nRazorpay Payment Signature :"+order.getPaymentDetails().getRazorpayPaymentSignature(),"Payment for you order with id "+order.getId()+" has been recieved");
        return new ResponseEntity<>("Sucess", HttpStatus.CREATED);
    }
}
