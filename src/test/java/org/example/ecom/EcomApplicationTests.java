package org.example.ecom;

import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.ForProducts.Images;
import org.example.ecom.Entity.ForProducts.Product;
import org.example.ecom.Entity.ForProducts.ProductModel;
import org.example.ecom.Entity.ForProducts.Size;
import org.example.ecom.Repository.OrderRepository;
import org.example.ecom.Repository.Product.CategoryRepository;
import org.example.ecom.Repository.Product.ProductRepository;
import org.example.ecom.Repository.RatingRepository;
import org.example.ecom.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class EcomApplicationTests {
//	@Autowired
//	ProductRepository repository;
//
//	@Test
//	void contextLoads() {
//		repository.deleteById(9L);
////		System.out.println(repository.findById(9L));
//	}
//	@Autowired
//	ProductService productService;
//
//
////	@Test
////	public void serviceTestAdd(){
////		ProductModel productModel=new ProductModel("Shirt",
////				"Petter England",
////				2000,
////				1500,
////				500,
////				"PRoduct Description",
////				"White",
////				List.of(Size.builder().name("L").build(),Size.builder().name("M").build())
////				,List.of(Images.builder().imageUrl("link").build()),
////				"Men","UpperWear","shirts","Plain",40
////		);
//		productService.createProduct(productModel);
//	}
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
	public void serviceTestUpdate(){
        Pageable pageable= PageRequest.of(0,30,Sort.by("id").descending());
        orderRepository.findById(51L,pageable).getContent().stream().forEach(orders -> System.out.println(orders));
        System.out.println();
	}

}
