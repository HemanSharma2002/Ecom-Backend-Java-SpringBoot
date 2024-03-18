package org.example.ecom.Controler;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Entity.RatingModel;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Service.RatingService;
import org.example.ecom.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class RatingControler {
    private UserService userService;
    private RatingService ratingService;

    public RatingControler(UserService userService, RatingService ratingService) {
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @PostMapping("/{productId}/rating")
    public Rating createRating(Authentication authentication, @PathVariable("productId") Long productId,@RequestBody RatingModel ratingModel) throws UserException, ProductException {

       User user= userService.findUserByUserName(authentication.getName());
       return ratingService.createRating(user.getId(),productId,ratingModel);
    }
    @GetMapping("/{productId}/rating")
    public List<Rating> getAllRattings( @PathVariable("productId") Long productId) throws UserException, ProductException {
        return ratingService.getAllRatings(productId);
    }
}
