package org.example.ecom.Service;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Exceptions.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
    public Product createProduct(ProductModel productModel);
    public String deleteProduct(Long id) throws ProductException;
    public Product updateProduct(Long id,Product product) throws ProductException;
    public Product findProductById(Long id)throws ProductException;
    public List<Product>findProductByCategory(String category);
    public Page<Product> getAllProduct(String category,List<String>colors,List<String>sizes,List<String>pattern,List<String >avilability,String sort,Integer pageNumber,Integer PageSize);

    Page<Product> searchProduct(String product,Integer pageNumber,Integer pageSize,String sort);
}
