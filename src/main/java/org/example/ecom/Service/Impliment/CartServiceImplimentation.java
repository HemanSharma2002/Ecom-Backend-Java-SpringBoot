package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.AddItemModel;
import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.CartItem;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.CartRepository;
import org.example.ecom.Repository.Product.ProductRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartItemService;
import org.example.ecom.Service.CartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImplimentation implements CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public CartServiceImplimentation(CartRepository cartRepository, CartItemService cartItemService, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart=Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }

    @Override
    public CartItem addCartItem(Long userId, AddItemModel request) throws ProductException, UserException {
        Optional<User> user=userRepository.findById(userId);
        Optional<Product> product=productRepository.findById(request.getProductId());
        if(user.isPresent()){
            if (product.isPresent()) {
                CartItem item = CartItem.builder()
                        .cart(user.get().getCart())
                        .product(product.get())
                        .size(request.getSize())
                        .dicountedPrice((double) (product.get().getDiscountedPrice() * request.getQuantity()))
                        .quantity(request.getQuantity())
                        .price((double) (product.get().getPrice() * request.getQuantity()))
                        .totalDiscount(product.get().getDiscountPresent()).build();
                CartItem cartItem=cartItemService.createCartItem(item);
//                System.out.println(cartItem.getCart());
//                cartTotalUpdater(cartItem.getCart());
                return cartItem;
            }
           throw  new ProductException("Froduct not found");
        }
        throw new  UserException("User not found"+userId);
    }

    @Override
    public Cart findUserCart(Long userId) throws UserException {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getCart();
        }
        throw  new UserException("invalid user id :"+userId);
    }
    @Override
    public void cartTotalUpdater(Cart cart){
        cart.setTotalPrice(0.0);
        cart.setTotalDiscountedPrice(0.0);
        cart.setTotalItem(0);
        cart.setTotalDiscount(0);
        cart.getCartItems().stream().forEach(cartItem->{
            cart.setTotalPrice(cart.getTotalPrice()+cartItem.getPrice());
            cart.setTotalDiscountedPrice(cart.getTotalDiscountedPrice()+cartItem.getDicountedPrice());
            cart.setTotalItem(cart.getTotalItem()+cartItem.getQuantity());
            cart.setTotalDiscount((cart.getTotalDiscount()+cartItem.getTotalDiscount())/2);
            System.out.println(cartItem.getId());
        });
        cartRepository.save(cart);
    }
}
