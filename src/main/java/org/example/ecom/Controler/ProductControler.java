package org.example.ecom.Controler;

import org.example.ecom.Entity.ForProducts.CatogoryResponse;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Service.ImageFileService;
import org.example.ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ProductControler {
    private ProductService productService;
    @Autowired
    private ImageFileService imageFileService;

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
    @GetMapping("/products/id/{productId}")
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
        System.out.println("In side Product");
        return new ResponseEntity<>(productService.createProduct(productModel),HttpStatus.ACCEPTED);
    }
    @GetMapping("/product/{category}")
    public ResponseEntity<List<Product>> getProductListByCategory(@PathVariable String category){
        return new ResponseEntity<>(productService.findProductByCategory(category),HttpStatus.ACCEPTED);
    }
    @PostMapping("/save")
    public Object uploadImage(@RequestParam("file")MultipartFile [] files){
        ArrayList<String> urls=new ArrayList<>();
        Arrays.stream(files).toList().forEach(file->
        {
            urls.add(imageFileService.saveImageToDatabase(file));
        });
        System.out.println("img");
        return urls;
    }
    @GetMapping("/products/ratings/{id}")
    public ResponseEntity<List<Rating>> getProductRatingsById(@PathVariable ("id") Long id) throws ProductException {
        return  new ResponseEntity<>(productService.getProductRatings(id),HttpStatus.OK);
    }
    @GetMapping("/products/ratings/stats/{id}")
    public ResponseEntity<List<?>> getProductRatingsStaticsById(@PathVariable ("id") Long id) throws ProductException {
        return  new ResponseEntity<>(productService.getProductRatingStats(id),HttpStatus.OK);
    }

    @GetMapping("/products/category")
    public ResponseEntity<List<CatogoryResponse>> getAllCategory()  {
        return  new ResponseEntity<>(productService.getAllCategory(),HttpStatus.OK);
    }

    //    {
    @PutMapping("/products/{top}/{page}")
    public ResponseEntity<Page<Product>>getProductByTopLevelcategory(@PathVariable String top,
                                                                     @PathVariable  Integer page,@RequestBody FilterRequest filter){
        return new ResponseEntity<>(productService.getProductbyCategory(top, "", "", filter.sort, page, filter.colors, filter.pattern),HttpStatus.OK);
    }
    @PutMapping("/products/{top}/{second}/{page}")
    public ResponseEntity<Page<Product>>getProductBySecondLevelcategory(@PathVariable String top,@PathVariable String second,
                                                                        @PathVariable  Integer page,@RequestBody FilterRequest filter){
        return new ResponseEntity<>(productService.getProductbyCategory(top, second, "", filter.sort, page, filter.colors, filter.pattern),HttpStatus.OK);
    }
    @PutMapping("/products/{top}/{second}/{third}/{page}")
    public ResponseEntity<Page<Product>>getProductByThirdLevelcategory(@PathVariable String top,@PathVariable String second,
                                                                     @PathVariable String third,
                                                                       @PathVariable Integer page,@RequestBody FilterRequest filter){
        return new ResponseEntity<>(productService.getProductbyCategory(top, second, third, filter.sort, page, filter.colors,filter.pattern),HttpStatus.OK);
    }

//    }
    record FilterRequest (String sort,List<String> colors,List<String> pattern){}

}
