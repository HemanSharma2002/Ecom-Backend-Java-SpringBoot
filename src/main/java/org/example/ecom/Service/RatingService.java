package org.example.ecom.Service;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Entity.RatingModel;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    public Rating createRating(Long userId, Long productId, RatingModel ratingModel)throws UserException,ProductException;
    public List<Rating> getAllRatings(Long productId)throws ProductException;
}
