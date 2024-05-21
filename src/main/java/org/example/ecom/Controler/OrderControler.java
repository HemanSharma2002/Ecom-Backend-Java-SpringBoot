package org.example.ecom.Controler;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.OrdersService;
import org.example.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class OrderControler {
    private OrdersService ordersService;
    private UserService userService;
    @Autowired
    private CartService cartService;

    public OrderControler(OrdersService ordersService, UserService userService) {
        this.ordersService = ordersService;
        this.userService = userService;
    }
    @PostMapping("/order/{online}")
    public ResponseEntity<Orders> createOrders(Authentication authentication, @RequestBody Address shippingAddress, @PathVariable Boolean online) throws OrderException, UserException, MessagingException {
        System.out.println(authentication.getName());
        User user=userService.findUserByUserName(authentication.getName());
        System.out.println(user.getEmail());
        return new ResponseEntity<>(ordersService.createOrders(user,shippingAddress,online), HttpStatus.CREATED);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Orders> findOrdersById(@PathVariable("orderId") Long ordersId) throws OrderException {
        return new ResponseEntity<>(ordersService.findOrdersById(ordersId), HttpStatus.OK);
    }
    @GetMapping("/order")
    public ResponseEntity<List<Orders>> usersOrdersHistory(Authentication authentication) throws UserException {
        User user=userService.findUserByUserName(authentication.getName());
        return new ResponseEntity<>(ordersService.usersOrdersHistory(user.getId()), HttpStatus.OK);
    }
    @PutMapping("/order/{orderId}/place")
    public ResponseEntity<Orders> placeOrders(@PathVariable("orderId") Long OrdersId) throws OrderException {
        return new ResponseEntity<>(ordersService.placeOrders(OrdersId), HttpStatus.OK);

    }
    @PutMapping("/order/{orderId}/confirm")
    public ResponseEntity<Orders> confirmOrders(@PathVariable("orderId") Long OrdersId) throws OrderException {
        return new ResponseEntity<>(ordersService.confirmOrders(OrdersId), HttpStatus.OK);

    }
    @PutMapping("/order/{orderId}/shipped")
    public ResponseEntity<Orders> shipedOrders(@PathVariable("orderId") Long OrdersId) throws OrderException {
        return new ResponseEntity<>(ordersService.shipedOrders(OrdersId), HttpStatus.OK);

    }
    @PutMapping("/order/{orderId}/delivered")
    public ResponseEntity<Orders> deliveredOrders(@PathVariable("orderId") Long OrdersId) throws OrderException {
        return new ResponseEntity<>(ordersService.deliveredOrders(OrdersId), HttpStatus.OK);

    }
    @PutMapping("/order/{orderId}/cancel")
    public ResponseEntity<Orders> cancelOrders(@PathVariable("orderId") Long OrdersId) throws OrderException {
        return new ResponseEntity<>(ordersService.cancelOrders(OrdersId), HttpStatus.OK);

    }
    @PostMapping("/address")
    public  ResponseEntity<Address> addUserAddress(@RequestBody Address address,Authentication authentication) throws UserException {
        return new ResponseEntity<Address>(userService.addUserAddress(address,authentication.getName()),HttpStatus.OK);
    }
    @GetMapping("/address")
    public  ResponseEntity<List<Address>> getAllUserAddress(Authentication authentication) throws UserException {
        return new ResponseEntity<List<Address>>(userService.getAllAddress(authentication.getName()),HttpStatus.OK);
    }
    @PutMapping("/address")
    public  HttpStatus updateCartAddress(@RequestBody Address address,Authentication authentication){
        cartService.updateSelectedAddress(address,authentication.getName());
        return HttpStatus.OK;
    }

}
