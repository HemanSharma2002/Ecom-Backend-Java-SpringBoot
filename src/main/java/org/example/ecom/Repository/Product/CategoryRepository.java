package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    Category findByNameAndParentCategory(String name, Category parentCategory);
}
