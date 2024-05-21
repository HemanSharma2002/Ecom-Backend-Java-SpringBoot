package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.ForProducts.*;
import org.example.ecom.Entity.Rating;
import org.example.ecom.Exceptions.ProductException;
import org.example.ecom.Repository.Product.CategoryRepository;
import org.example.ecom.Repository.Product.ImageRepository;
import org.example.ecom.Repository.Product.ProductRepository;
import org.example.ecom.Repository.Product.SizeRepository;
import org.example.ecom.Repository.RatingRepository;
import org.example.ecom.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImplement implements ProductService {
    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private CategoryRepository categoryRepository;
    private SizeRepository sizeRepository;
    private RatingRepository ratingRepository;

    public ProductServiceImplement(ProductRepository productRepository, ImageRepository imageRepository, CategoryRepository categoryRepository, SizeRepository sizeRepository, RatingRepository ratingRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
        this.sizeRepository = sizeRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Product createProduct(ProductModel productModel) {
        if(productModel.getTopLevelCategory().isEmpty() && productModel.getSecondLevelCategory().isEmpty() && productModel.getThirdLevelCategory().isEmpty() ){
            return null;
        }
        System.out.println(
                "top"
        );
        Category topLevel=categoryRepository.findByName(productModel.getTopLevelCategory());
        if(topLevel==null){
            topLevel=Category.builder().level(1)
                    .name(productModel.getTopLevelCategory()).build();
            categoryRepository.save(topLevel);
        }
        Category secondLevel=categoryRepository.findByNameAndParentCategory(productModel.getSecondLevelCategory(),topLevel);
        if(secondLevel==null){
            secondLevel=Category.builder().level(2)
                    .name(productModel.getSecondLevelCategory())
                    .parentCategory(topLevel)
                    .build();
            categoryRepository.save(secondLevel);
        }
        Category thirdLevel=categoryRepository.findByNameAndParentCategory(productModel.getThirdLevelCategory(),secondLevel);
        if(thirdLevel==null){
            thirdLevel=Category.builder().level(3)
                    .name(productModel.getThirdLevelCategory())
                    .parentCategory(secondLevel)
                    .build();
            categoryRepository.save(thirdLevel);
        }
        Product product=new Product(
                productModel.getTitle(),productModel.getBrand(),productModel.getPrice(),productModel.getDiscountedPrice(),
                productModel.getDiscountPresent(),productModel.getDescription(),productModel.getColor(),
                thirdLevel,productModel.getPattern(),productModel.getQuantity());
        product.setAvgRating(0.0);
        product.setTotalRating(0);
//        System.out.println(product);
        Product savedProduct=productRepository.save(product);
       if(!productModel.getSizes().isEmpty()){
           productModel.getSizes().forEach(size -> {
               Size size1= Size.builder().name(size.getName()).quantity(size.getQuantity()).product(savedProduct).build();
               sizeRepository.save(size1);
           });
       }
        if(!productModel.getImages().isEmpty()){
            productModel.getImages().forEach(image->{
                Images images=Images.builder().imageUrl(image).product(savedProduct).build();
                imageRepository.save(images);
            });
        }
        System.out.println("savedProduct");
        return productRepository.findById(savedProduct.getId()).get();
    }

    @Override
    public String deleteProduct(Long id) throws ProductException {
        Product product =findProductById(id);
        if(product!=null){
            productRepository.delete(product);
            return "Deletion Sucess";
        }
        return "Failed";
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductException {
        Optional<Product> oldProduct=productRepository.findById(id);
        if (oldProduct.isPresent()){
            return productRepository.save(product);
        }
        throw new ProductException("Product Not Found"+id);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt=productRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product Not Found"+id);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return productRepository.findByCategory(categoryRepository.findByName(category));
    }

    @Override
    public Page<Product> searchProduct(String product,Integer pageNumber,Integer pageSize,String sort) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);

        List<Product> products;
        if(sort.equals("price_low")){
            products = productRepository.findByTitleContainingOrderByDiscountedPriceAsc(product);
            
        } else if (sort.equals("price_high")) {
            products = productRepository.findByTitleContainingOrderByDiscountedPriceDesc(product);
        }
        else {
            products = productRepository.findByTitleContaining(product);
        }

        int startIndex=(int)pageable.getOffset();
        int endIndex=Math.min(startIndex+pageable.getPageSize(),products.size());
        List<Product> pageContent=products.subList(startIndex,endIndex);
        Page<Product>filterdProducts=new PageImpl<>(pageContent,pageable,products.size());
        return filterdProducts;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, List<String> pattern, List<String> avilability, String sort, Integer pageNumber, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        List<Product> products=productRepository.filterProducts(category,sort,10,100000);
        if(!colors.isEmpty()){
            products=products.stream().filter(product-> colors.contains(product.getColor())).toList();
        }
        if(!pattern.isEmpty()){
            products=products.stream().filter(product-> pattern.contains(product.getPattern())).toList();
        }
        if(!avilability.isEmpty()){
            products=products.stream().filter(product-> {
                if(avilability.contains("Avilable") && product.getQuantity()>0){
                    return true;
                }
                else if(avilability.contains("Out of Stock") && product.getQuantity()==0){
                    return true;
                }
                return false;
            }).toList();
        }
        int startIndex=(int)pageable.getOffset();
        int endIndex=Math.min(startIndex+pageable.getPageSize(),products.size());
        List<Product> pageContent=products.subList(startIndex,endIndex);
        Page<Product>filterdProducts=new PageImpl<>(pageContent,pageable,products.size());

        return filterdProducts;
    }

    @Override
    public List<Rating> getProductRatings(Long productId) throws ProductException {
        Product product=findProductById(productId);
        if(product==null){
            throw new ProductException("Product not present id: "+productId);
        }
        return product.getRatings();
    }

    @Override
    public List<?> getProductRatingStats(Long productId) throws ProductException {
        Product product=findProductById(productId);
        if(product==null){
            throw new ProductException("Product not present id: "+productId);
        }
        if(product.getRatings().isEmpty()){
            return new ArrayList<>();
        }
        return ratingRepository.getStaticsOfProductRatings(productId);
    }

    @Override
    public List<CatogoryResponse> getAllCategory() {
        List<CatogoryResponse> level1=new ArrayList<>();
        List<Category> top=categoryRepository.getAllTopLevelcatogories();
        top.forEach(element->{
            List<CatogoryResponse>level2=new ArrayList<>();
            List<Category> second=categoryRepository.getSecondLevelCatogoryForRespectiveTopLevelCatogory(element.getName());
            second.forEach(element2->{
                List<CatogoryResponse> level3=new ArrayList<>();
                List<Category> third=categoryRepository.getThirdLevelCatogoryForRespectiveTopLevelCatogoryAndSecond(element.getName(),element2.getName());
                third.forEach(element3->{
                    level3.add(new CatogoryResponse(element3.getName(),"/products/"+element.getName()+"/"+element2.getName()+"/"+element3.getName(),null));
                });
                level2.add(new CatogoryResponse(element2.getName(),"/products/"+element.getName()+"/"+element2.getName(),level3));
            });
            level1.add(new CatogoryResponse(element.getName(),"/products/"+element.getName(),level2));
        });
        return level1;
    }

    @Override
    public Page<Product> getProductbyCategory(String top, String second, String third, String sort, Integer pageno, List<String> colors, List<String> pattern) {
        Pageable pageable= PageRequest.of(pageno,12);
        List<Product> products;
        if (!top.isEmpty()&&!second.isEmpty()&&!third.isEmpty()){
            products=productRepository.findProductByThirdLevelCategory(top, second, third, sort);
        }
        else if (!top.isEmpty()&&!second.isEmpty()) {
            products=productRepository.findProductBySecondLevelCatogory(top, second, sort);
        }
        else {
            products=productRepository.findProductByTopCategory(top, sort);
        }
        if(!colors.isEmpty()){
            products=products.stream().filter(product-> colors.contains(product.getColor())).toList();
        }
        if(!pattern.isEmpty()){
            products=products.stream().filter(product-> pattern.contains(product.getPattern())).toList();
        }


        int startIndex=(int)pageable.getOffset();
        int endIndex=Math.min(startIndex+pageable.getPageSize(),products.size());
        List<Product> pageContent=products.subList(startIndex,endIndex);
        Page<Product>filterdProducts=new PageImpl<>(pageContent,pageable,products.size());
        return  filterdProducts;
    }
}
