package org.example.ecom.Service;

import org.example.ecom.Entity.AddItemModel;
import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.CartItem;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public Cart createCart(User user);
    public CartItem addCartItem(Long userId, AddItemModel request)throws ProductException, UserException;
    public Cart findUserCart(Long userId)throws UserException;
    public void cartTotalUpdater(Cart cart);
    public void updateSelectedAddress(Address address,String username);
}
