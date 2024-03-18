package org.example.ecom.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingModel {
    private Double rating;
    private String review;
    private LocalDateTime createdAt;

    public RatingModel(Double rating, String review) {
        this.rating = rating;
        this.review = review;
        this.createdAt = LocalDateTime.now();
    }
}
