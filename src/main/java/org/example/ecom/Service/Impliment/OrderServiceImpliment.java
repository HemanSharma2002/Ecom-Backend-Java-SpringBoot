package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.*;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.OlderException;
import org.example.ecom.Repository.CartRepository;
import org.example.ecom.Repository.OrderItemRepository;
import org.example.ecom.Repository.OrderRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.OrdersService;
import org.example.ecom.Service.ProductService;
import org.example.ecom.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpliment implements OrdersService {
    private CartRepository cartRepository;
    private CartService cartService;
    private ProductService productService;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    public OrderServiceImpliment(CartRepository cartRepository, CartService cartService, ProductService productService, UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Orders createOrders(User user, Address shippingAddress) throws OlderException {
        Cart cart=user.getCart();
        Orders order=new Orders(user,
                new ShippingAddress(shippingAddress.getFirstName(), shippingAddress.getLastName(),shippingAddress.getStreetAddress(),shippingAddress.getCity(),
                        shippingAddress.getState(),shippingAddress.getPincode(),shippingAddress.getMobile()),
                cart.getTotalPrice(),cart.getTotalDiscountedPrice() , cart.getTotalDiscount(), cart.getTotalItem());
        order=orderRepository.save(order);
        for(CartItem item:user.getCart().getCartItems()){
            OrderItem orderItem=new OrderItem(order,item.getProduct(),item.getSize(),item.getQuantity(),item.getPrice(),item.getDicountedPrice(),user.getId());
            orderItemRepository.save(orderItem);
        }
        return orderRepository.findById(order.getId()).get();
    }

    @Override
    public Orders findOrdersById(Long OrdersId) throws OlderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            return orders.get();
        }
        throw new OlderException("Order not found "+OrdersId);
    }
    @Override
    public List<Orders> usersOrdersHistory(Long userId) {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getOrders();
        }
        return null;
    }

    @Override
    public Orders placeOrders(Long OrdersId) throws OlderException {
        return null;
    }

    @Override
    public Orders confirmOrders(Long OrdersId) throws OlderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus("CONFIRMED");
            return orderRepository.save(orders.get());
        }
        throw new OlderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders shipedOrders(Long OrdersId) throws OlderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus("SHIPPED");
            return orderRepository.save(orders.get());
        }
        throw new OlderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders deliveredOrders(Long OrdersId) throws OlderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus("DELIVERED");
            return orderRepository.save(orders.get());
        }
        throw new OlderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders cancelOrders(Long OrdersId) throws OlderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus("CANCELED");
            return orderRepository.save(orders.get());
        }
        throw new OlderException("Order not found :"+OrdersId);
    }
}
