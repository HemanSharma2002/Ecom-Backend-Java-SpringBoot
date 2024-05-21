package org.example.ecom.Controler;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminControler {
    private AdminService adminService;
    @GetMapping("/order/{orderStatus}/{pageNo}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Orders> getAllOrders(@PathVariable OrderStatus orderStatus,@PathVariable Integer pageNo) throws OrderException {
        return adminService.getAllOrders(pageNo, orderStatus);
    }
    @GetMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Orders> getOrderById(@PathVariable Long orderId) throws OrderException {
        return adminService.getOrderByOrderId(0,orderId);
    }
    @GetMapping("/user/{pageNo}")
    @ResponseStatus(HttpStatus.OK)
    public Page<User> getUsers(@PathVariable  Integer pageNo){
        return adminService.getAllUsers(pageNo);
    }
    @PutMapping("/order/{orderId}/{orderStatus}")
    @ResponseStatus(HttpStatus.CREATED)
    public String updateOrderStatusById(@PathVariable Long orderId,@PathVariable OrderStatus orderStatus) throws OrderException, MessagingException {
        return adminService.updateOrderStatus(orderId, orderStatus);
    }
    @PutMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public String updateProduct(@RequestBody Product product) throws ProductException {
        return adminService.updateProduct(product);
    }
}
