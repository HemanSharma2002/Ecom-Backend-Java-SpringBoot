package org.example.ecom.Service;

import org.example.ecom.Entity.ForProducts.CatogoryResponse;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Exceptions.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<Rating> getProductRatings(Long productId)throws ProductException;
    public List<?>getProductRatingStats(Long productId) throws ProductException;
    public List<CatogoryResponse>getAllCategory();
    public Page<Product> getProductbyCategory(String top,String second,String third,String sort,Integer pageno
    ,List<String> colors, List<String> pattern);
}
