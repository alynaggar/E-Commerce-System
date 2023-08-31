package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Product;
import com.example.amazon.Services.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getProduct(@PathVariable long id){
        return productService.getById(id);
    }

    @GetMapping("")
    public AmazonResponseEntity<?> getAllProducts(){
        return productService.getAll();
    }

    @PostMapping("")
    public AmazonResponseEntity<?> addProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @PutMapping("")
    public AmazonResponseEntity<?> updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }
}
