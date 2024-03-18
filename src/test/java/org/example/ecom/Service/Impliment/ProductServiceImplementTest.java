package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.ForProducts.Images;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Entity.ForProducts.Size;
import org.example.ecom.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductServiceImplementTest {
    ProductService productService;

    public ProductServiceImplementTest(ProductService productService) {
        this.productService = productService;
    }



}