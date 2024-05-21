package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.OrderItem;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Entity.RatingModel;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.OrderItemRepository;
import org.example.ecom.Repository.RatingRepository;
import org.example.ecom.Service.ProductService;
import org.example.ecom.Service.RatingService;
import org.example.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpliment implements RatingService {
    private RatingRepository ratingRepository;
    private UserService userService;
    private ProductService productService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public RatingServiceImpliment(RatingRepository ratingRepository, UserService userService, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Rating createRating(Long userId, Long productId,RatingModel ratingModel,Long orderId) throws UserException, ProductException {
        User user=userService.findUserById(userId);
        Product product=productService.findProductById(productId);
        Optional<OrderItem> orderItem=orderItemRepository.findById(orderId);
        if(!orderItem.isEmpty()) {
            orderItem.get().setReviewed(true);
            orderItemRepository.save(orderItem.get());
        }
        if(!product.getRatings().isEmpty()){
            product.setTotalRating(product.getTotalRating() + 1);
            product.setAvgRating(((product.getAvgRating() * (product.getTotalRating() - 1)) + ratingModel.getRating()) / product.getTotalRating());
        }
        else {
            product.setTotalRating(1);
            product.setAvgRating(ratingModel.getRating());
        }
        return ratingRepository.save(new Rating(user,product, ratingModel.getRating(), ratingModel.getReview(),user.getUsername()));
    }

    @Override
    public List<Rating> getAllRatings(Long productId) throws ProductException {
        return productService.findProductById(productId).getRatings();
    }
}
