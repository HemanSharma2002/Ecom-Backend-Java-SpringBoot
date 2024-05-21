package org.example.ecom.Service.Impliment;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Repository.OrderRepository;
import org.example.ecom.Repository.Product.ProductRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.AdminService;
import org.example.ecom.Service.EmailService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImp  implements AdminService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private EmailService emailService;
    @Override
    public Page<Orders> getAllOrders(Integer pageNo, OrderStatus orderstatus) throws OrderException {
        Pageable pageable= PageRequest.of(pageNo,30,Sort.by("id").descending());
        Page<Orders> ordersList;
        if(orderstatus==OrderStatus.ALL)
            ordersList=orderRepository.findAll(pageable);
        else
            ordersList=orderRepository.findByOrderStatus(orderstatus,pageable);

        return ordersList;
    }

    @Override
    public Page<Orders> getOrderByOrderId(Integer pageNo, Long orderId) throws OrderException {
        Pageable pageable = PageRequest.of(pageNo, 30, Sort.by("id").descending());
        return orderRepository.findById(orderId, pageable);
    }

    @Override
    public Page<User> getAllUsers(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 30);
        return userRepository.findAll(pageable);
    }

    @Override
    public String updateProduct(Product newProduct) throws ProductException {
        Optional<Product> product=productRepository.findById(newProduct.getId());
        if(product.isPresent()){
            if(newProduct==null){
                throw new ProductException("Invalid product is provided");
            }
            if(productRepository.save(newProduct)!=null){
                return "Updated";
            }else
                return "Failed to update";
        }
        else {
            throw new ProductException("Product not found ="+newProduct.getId());
        }
    }

    @Override
    public String updateOrderStatus(Long orderId, OrderStatus orderStatus) throws OrderException, MessagingException {
        Optional<Orders> orders=orderRepository.findById(orderId);
        if(orders.isPresent()){
            orders.get().setOrderStatus(orderStatus);
            if(orderStatus==OrderStatus.DELIVERED)
                orders.get().setDeliveryDate(LocalDateTime.now());
            orderRepository.save(orders.get());
            emailService.sendEmailWithoutAttachment(orders.get().getUser().getEmail(),"Dear "+orders.get().getUser().getFirstName()+"\nYour order is  "+orderStatus,"Your order with order id:#"+orderId+" has been "+orderStatus);
            return "Updated";
        }
        throw  new OrderException("Order not present ="+orderId);
    }
}
