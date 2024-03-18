package org.example.ecom.Service;
import org.aspectj.weaver.ast.Or;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Orders;
import org.example.ecom.Exceptions.OlderException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrdersService {
    public Orders createOrders(User user, Address shippingAddress) throws OlderException;
    public Orders findOrdersById(Long OrdersId) throws OlderException;
    public List<Orders> usersOrdersHistory(Long userId);
    public Orders placeOrders(Long OrdersId) throws OlderException;
    public Orders confirmOrders(Long OrdersId) throws OlderException;
    public Orders shipedOrders(Long OrdersId) throws OlderException;
    public Orders deliveredOrders(Long OrdersId) throws OlderException;
    public Orders cancelOrders(Long OrdersId) throws OlderException;
}
