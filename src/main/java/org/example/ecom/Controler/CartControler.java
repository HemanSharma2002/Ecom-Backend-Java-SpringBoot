package org.example.ecom.Controler;

import org.example.ecom.Entity.AddItemModel;
import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.CartItem;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.CartItemException;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.CartRepository;
import org.example.ecom.Service.CartItemService;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class CartControler {
    private CartService cartService;
    private UserService userService;
    private CartItemService cartItemService;

    public CartControler(CartService cartService, UserService userService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.cartItemService = cartItemService;
    }


    @GetMapping("/cart")
    public ResponseEntity<Cart>findUserCart(Authentication authentication) throws UserException {
        User user=userService.findUserByUserName(authentication.getName());
        return new ResponseEntity<>(user.getCart(), HttpStatus.OK);
    }
    @PostMapping("/cart")
    public ResponseEntity<String>addItemToCart(Authentication authentication, @RequestBody AddItemModel addItemModel) throws UserException, ProductException {
        User user=userService.findUserByUserName(authentication.getName());
        if(addItemModel.getQuantity()==0) {
            return new ResponseEntity<>("Fail", HttpStatus.EXPECTATION_FAILED);
        }
        CartItem cart=cartService.addCartItem(user.getId(), addItemModel);
        if(cart!=null){
            return new ResponseEntity<>("Sucess", HttpStatus.OK);
        }
        return new ResponseEntity<>("Fail", HttpStatus.EXPECTATION_FAILED);
    }
    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<String>deleteItemFromCart(Authentication authentication,@PathVariable ("cartItemId") Long cartItemId) throws UserException, ProductException, CartItemException {
        System.out.println(cartItemId);
        User user=userService.findUserByUserName(authentication.getName());
        cartItemService.removeCartItem(user.getId(), cartItemId);
        return new ResponseEntity<>("Sucess", HttpStatus.OK);
    }
    @PutMapping("/cart")
    public ResponseEntity<CartItem>updateItemFromCart(Authentication authentication,@RequestBody CartItem cartItemOld) throws UserException, ProductException, CartItemException {
        User user=userService.findUserByUserName(authentication.getName());
        CartItem cartItem=cartItemService.updateCartItem(user.getId(),cartItemOld.getId(),cartItemOld.getQuantity());
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

//    @Autowired
//    private CartRepository cartRepository;
//    @DeleteMapping("/{cart}")
//    public String test(Authentication authentication,@PathVariable Long cart){
//        cartRepository.deleteById(cart);
//        return "Happy";
//    }
}
