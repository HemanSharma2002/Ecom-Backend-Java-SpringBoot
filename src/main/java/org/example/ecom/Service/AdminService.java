package org.example.ecom.Service;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    public Page<Orders>getAllOrders(Integer pageNo, OrderStatus orderstatus)throws OrderException;
    public Page<Orders>getOrderByOrderId(Integer pageNo ,Long orderId) throws OrderException;
    public Page<User>getAllUsers(Integer pageNo);
    public String updateProduct(Product product)throws ProductException;
    public String updateOrderStatus(Long orderId,OrderStatus orderStatus) throws OrderException, MessagingException;
}
