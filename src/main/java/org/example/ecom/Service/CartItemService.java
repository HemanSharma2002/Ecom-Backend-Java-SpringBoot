package org.example.ecom.Service;

import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.CartItem;
import org.example.ecom.Exceptions.CartItemException;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {
    public CartItem createCartItem(CartItem cart);
    public CartItem updateCartItem(Long userID,Long is,Integer quantity) throws CartItemException, UserException;
    public CartItem createCartExist(Long userID,Long is,String size ,CartItem cartItem);
    public void removeCartItem(Long userId,Long cartItemsId)throws CartItemException,UserException;
    public CartItem findCartItemsById(Long cartItemId)throws CartItemException;
    public void cartTotalUpdater(Cart cart,Double tp,Double dp,Integer q,Integer d);
}
