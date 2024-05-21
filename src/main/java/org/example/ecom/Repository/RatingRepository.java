package org.example.ecom.Repository;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.Stastics;
import org.example.ecom.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {
    @Query("select rating,count(id) count from Rating  where product.id=:pId group by rating order by rating")
    public List<?> getStaticsOfProductRatings(@Param("pId") Long pID);
    @Query("select r from Rating r where r.product.id=:productId")
    public List<Rating> getListOfRatingByProductId(@Param("productId")Long pid);

}
