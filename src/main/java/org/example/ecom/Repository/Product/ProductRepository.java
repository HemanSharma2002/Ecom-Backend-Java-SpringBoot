package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.Category;
import org.example.ecom.Entity.ForProducts.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p from Product p " +
            "where (p.category.name=:category or :category='') " +
            "and ((:minPrice is null and :maxPrice is null) or (p.discountedPrice between :minPrice and :maxPrice))" +
            "order by " +
            "case  when :sort='price_low' then p.discountedPrice end asc," +
            "case  when :sort='price_high' then p.discountedPrice end desc ")
    public List<Product> filterProducts(@Param("category") String category,@Param("sort") String sort,@Param("minPrice")Integer minPrice
            ,@Param("maxPrice") Integer maxPrice);
    public List<Product> findByTitleContaining(String search);
    public List<Product> findByTitleContainingOrderByDiscountedPriceAsc(String search);
    public List<Product> findByTitleContainingOrderByDiscountedPriceDesc(String search);

//    find ProductsBy categorys

    public List<Product> findByCategory(Category category);
    @Query("select p from Product  p " +
            "where p.category.parentCategory.parentCategory.name=:top" +
            " order by " +
            "case  when :sort='price_low' then p.discountedPrice end asc," +
            "case  when :sort='price_high' then p.discountedPrice end desc ")
    public List<Product>findProductByTopCategory(@Param("top") String top,@Param("sort") String sort);
    @Query("select p from Product  p " +
            "where p.category.parentCategory.parentCategory.name=:top and p.category.parentCategory.name=:second" +
            " order by " +
            "case  when :sort='price_low' then p.discountedPrice end asc," +
            "case  when :sort='price_high' then p.discountedPrice end desc ")
    public List<Product>findProductBySecondLevelCatogory(@Param("top") String top,@Param("second") String second,@Param("sort") String sort);
    @Query("select p from Product  p " +
            "where p.category.parentCategory.parentCategory.name=:top and p.category.parentCategory.name=:second and p.category.name=:third" +
            " order by " +
            "case  when :sort='price_low' then p.discountedPrice end asc," +
            "case  when :sort='price_high' then p.discountedPrice end desc ")
    public List<Product>findProductByThirdLevelCategory(@Param("top") String top , @Param("second") String second,@Param("third") String third , @Param("sort") String sort);


}
