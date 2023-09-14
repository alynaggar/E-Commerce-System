package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Product;
import com.example.amazon.Services.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getProduct(@PathVariable long id){
        return productService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("")
    public AmazonResponseEntity<?> getAllProducts(){
        return productService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public AmazonResponseEntity<?> addProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("")
    public AmazonResponseEntity<?> updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }
}
