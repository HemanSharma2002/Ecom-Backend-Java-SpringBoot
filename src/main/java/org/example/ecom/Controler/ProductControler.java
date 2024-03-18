package org.example.ecom.Controler;

import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class ProductControler {
    private ProductService productService;

    public ProductControler(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<Page<Product>>findProductByCategoryHandler(@RequestParam String category,@RequestParam  List<String> colors
            ,@RequestParam  List<String> pattern,@RequestParam  List<String> avilability
            ,@RequestParam  String sort,@RequestParam  Integer pageNumber) {
        Page<Product> products=productService.getAllProduct(category,colors,null,pattern,avilability,sort,pageNumber,12);
        System.out.println("Product completed");
        return new ResponseEntity<>(products, HttpStatus.ACCEPTED);
    }
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product=productService.findProductById(productId);
        return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
    }
    @PostMapping("/products/search")
    public ResponseEntity<Page<Product>> findProductByIdHandler(@RequestParam String search,@RequestParam Integer pageNo) throws ProductException {
        System.out.println(search);
        Page products=productService.searchProduct(search,pageNo,12,"");
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @PostMapping("/products/add")
    public ResponseEntity<Product> addProduct(@RequestBody ProductModel productModel){
        return new ResponseEntity<>(productService.createProduct(productModel),HttpStatus.ACCEPTED);
    }
}
