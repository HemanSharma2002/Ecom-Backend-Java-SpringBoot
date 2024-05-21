package org.example.ecom.Service.Impliment;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.*;
import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.Enum.Payment;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.Size;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.CartItemRepository;
import org.example.ecom.Repository.CartRepository;
import org.example.ecom.Repository.OrderItemRepository;
import org.example.ecom.Repository.OrderRepository;
import org.example.ecom.Repository.Product.ProductRepository;
import org.example.ecom.Repository.Product.SizeRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.EmailService;
import org.example.ecom.Service.OrdersService;
import org.example.ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpliment implements OrdersService {
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    private CartService cartService;
    private ProductService productService;
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;

    public OrderServiceImpliment(CartRepository cartRepository, CartService cartService, ProductService productService, UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Orders createOrders(User user, Address shippingAddress,Boolean online) throws OrderException, MessagingException {
        Cart cart=user.getCart();
        Orders order=new Orders(user,
                new ShippingAddress(shippingAddress.getFirstName(), shippingAddress.getLastName(),shippingAddress.getStreetAddress(),shippingAddress.getCity(),
                        shippingAddress.getState(),shippingAddress.getPincode(),shippingAddress.getMobile()),
                cart.getTotalPrice(),cart.getTotalDiscountedPrice() , cart.getTotalDiscount(), cart.getTotalItem());
        if(online){
            order.setPaymentType(Payment.ONLINE);
        }else {
            order.setPaymentType(Payment.POD);
        }
        order=orderRepository.save(order);
        for(CartItem item:user.getCart().getCartItems()){
            OrderItem orderItem=new OrderItem(order,item.getProduct(),item.getSize(),item.getQuantity(),item.getPrice(),item.getDicountedPrice(),user.getId());
            Product product=item.getProduct();
            product.setQuantity(product.getQuantity()-item.getQuantity());
            productRepository.save(product);
            orderItemRepository.save(orderItem);
        }
        if(!online){
            confirmOrders(order.getId());
        }
        Cart Cart=user.getCart();
        cart.getCartItems().forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        cart.setTotalPrice(0.0);
        cart.setTotalDiscount(0);
        cart.setTotalDiscountedPrice(0.0);
        cart.setTotalItem(0);
        cartRepository.save(cart);
        System.out.println(cart.getUser().getEmail());
        emailService.sendEmailWithoutAttachment(cart.getUser().getEmail(),"Dear "+order.getUser().getFirstName()+"\nYour order with order id:"+order.getId()+" has been" +
                "placed .\nYour total billing ammount is "+order.getTotalDiscountedPrice()+" Your order will me delivered with in 3 to 5 working days \nThank You","Order has been placed");
        return orderRepository.findById(order.getId()).get();
    }

    @Override
    public Orders findOrdersById(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            return orders.get();
        }
        throw new OrderException("Order not found "+OrdersId);
    }
    @Override
    public List<Orders> usersOrdersHistory(Long userId) throws UserException {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getOrders();
        }
        throw new UserException("User not found");
    }

    @Override
    public Orders placeOrders(Long OrdersId) throws OrderException {
        return null;
    }

    @Override
    public Orders confirmOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.PLACED);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders shipedOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.SHIPPED);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders deliveredOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.DELIVERED);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }

    @Override
    public Orders cancelOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.CANCELED);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }
    @Override
    public Orders returnlOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.RETURN);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }
    @Override
    public Orders returnedOrders(Long OrdersId) throws OrderException {
        Optional<Orders> orders=orderRepository.findById(OrdersId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(OrderStatus.RETURNED);
            return orderRepository.save(orders.get());
        }
        throw new OrderException("Order not found :"+OrdersId);
    }
}
