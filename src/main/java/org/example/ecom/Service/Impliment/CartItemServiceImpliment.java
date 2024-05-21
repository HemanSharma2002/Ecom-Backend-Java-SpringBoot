package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.CartItem;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.CartItemException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.CartItemRepository;
import org.example.ecom.Repository.CartRepository;
import org.example.ecom.Service.CartItemService;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpliment implements CartItemService {
    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;

    public CartItemServiceImpliment(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cart) {
        CartItem cartItem = cartItemRepository.save(cart);
        System.out.println(cartItem.getCart());
        cartTotalUpdater(cart.getCart(), cartItem.getPrice(), cartItem.getDicountedPrice(), cartItem.getQuantity(), cartItem.getTotalDiscount());
        return cartItem;
    }

    @Override
    public CartItem updateCartItem(Long userID, Long id,Integer quantity) throws CartItemException, UserException {
        User user=userService.findUserById(userID);
        Optional<CartItem> cartItem1=cartItemRepository.findById(id);
        if(cartItem1.isPresent() && cartItem1.get().getCart().getUser()==user){
            cartItem1.get().setQuantity(quantity);
            cartItem1.get().setPrice((double) (cartItem1.get().getProduct().getPrice()*quantity));
            cartItem1.get().setDicountedPrice((double) (cartItem1.get().getProduct().getDiscountedPrice()*quantity));
            CartItem cartItem=cartItemRepository.save(cartItem1.get());
            System.out.println(cartItem);
            cartTotalUpdater(user.getCart(),0.0,0.0,0,0);
            return cartItem;
        }
        throw new CartItemException("Invalid Item");
    }

    @Override
    public CartItem createCartExist(Long userID, Long id, String size, CartItem cartItem) {
        return null;
    }

    @Override
     public void removeCartItem(Long userId, Long cartItemsId) throws CartItemException, UserException {
        User user=userService.findUserById(userId);
        {
            Optional<CartItem> cartItem=cartItemRepository.findById(cartItemsId);
            if(cartItem.isPresent() && user.equals(cartItem.get().getCart().getUser())){
                cartItem.get().getCart().setTotalPrice(cartItem.get().getCart().getTotalPrice()-cartItem.get().getPrice());
                cartItem.get().getCart().setTotalDiscountedPrice(cartItem.get().getCart().getTotalDiscountedPrice()-cartItem.get().getDicountedPrice());
                cartItem.get().getCart().setTotalDiscount(((cartItem.get().getCart().getTotalDiscount()*cartItem.get().getCart().getTotalItem())-cartItem.get().getTotalDiscount())/Math.max((cartItem.get().getCart().getTotalItem()-1),1));
                cartItem.get().getCart().setTotalItem(cartItem.get().getCart().getTotalItem()-cartItem.get().getQuantity());
                cartRepository.save(cartItem.get().getCart());
                cartItemRepository.deleteById(cartItemsId);

                return;
            }

        }
        throw new CartItemException("Invalid Item");
    }

    @Override
    public CartItem findCartItemsById(Long cartItemId) throws CartItemException {
        Optional<CartItem> cartItem=cartItemRepository.findById(cartItemId);
        if(cartItem.isPresent()){
            return cartItem.get();
        }
        throw new CartItemException("Invalid Item");
    }
    @Override
    public void cartTotalUpdater(Cart cart,Double tp,Double dp,Integer q,Integer d){
        cart.setTotalPrice(tp);
        cart.setTotalDiscountedPrice(dp);
        cart.setTotalItem(q);
        cart.setTotalDiscount(d);
        cart.getCartItems().stream().forEach(cartItem->{
            cart.setTotalPrice(cart.getTotalPrice()+cartItem.getPrice());
            cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()+cartItem.getDicountedPrice());
            cart.setTotalItem(cart.getTotalItem()+cartItem.getQuantity());
            cart.setTotalDiscount((cart.getTotalDiscount()+(cartItem.getTotalDiscount()*cartItem.getQuantity())));
        });
        cart.setTotalDiscount(cart.getTotalDiscount()/cart.getTotalItem());

        cartRepository.save(cart);
    }
    @Override
    public CartItem isProductAlreadyPresentInCart(Cart cart, Product product,String size){
        CartItem item=cartItemRepository.findByCartAndProductAndSize(cart,product,size);

        if(item!=null){
            return item;
        }
        else {
            return null;
        }
    }
}
