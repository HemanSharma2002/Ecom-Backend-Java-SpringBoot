package org.example.ecom.Entity.ForProducts;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private String title;
    private String brand;
    private Integer price;
    private Integer discountedPrice;
    private Integer discountPresent;
    private String description;
    private String color;
    private List<Size> sizes;
    private List<String> images;
    private String topLevelCategory;
    private String secondLevelCategory;
    private String thirdLevelCategory;
    private String pattern;
    private Integer quantity;
}
