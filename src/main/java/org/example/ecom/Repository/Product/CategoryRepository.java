package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    Category findByNameAndParentCategory(String name, Category parentCategory);
    @Query("select c from Category c " +
            "where c.level=1")
    List<Category>getAllTopLevelcatogories();
    @Query("select c from Category c " +
            "where c.level=2 and c.parentCategory.name=:top")
    List<Category>getSecondLevelCatogoryForRespectiveTopLevelCatogory(@Param("top") String top);
    @Query("select c from Category c " +
            "where c.level=3 and c.parentCategory.name=:second and c.parentCategory.parentCategory.name=:top")
    List<Category>getThirdLevelCatogoryForRespectiveTopLevelCatogoryAndSecond(@Param("top") String top,@Param("second") String second);
}
