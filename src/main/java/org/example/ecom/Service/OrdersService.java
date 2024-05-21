package org.example.ecom.Service;
import jakarta.mail.MessagingException;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OrderException;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrdersService {
    public Orders createOrders(User user, Address shippingAddress,Boolean online) throws OrderException, MessagingException;
    public Orders findOrdersById(Long OrdersId) throws OrderException;
    public List<Orders> usersOrdersHistory(Long userId) throws UserException;
    public Orders placeOrders(Long OrdersId) throws OrderException;
    public Orders confirmOrders(Long OrdersId) throws OrderException;
    public Orders shipedOrders(Long OrdersId) throws OrderException;
    public Orders deliveredOrders(Long OrdersId) throws OrderException;
    public Orders cancelOrders(Long OrdersId) throws OrderException;
    public Orders returnlOrders(Long OrdersId) throws OrderException;
    public Orders returnedOrders(Long OrdersId) throws OrderException;
}
