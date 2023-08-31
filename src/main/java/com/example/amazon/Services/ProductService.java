package com.example.amazon.Services;

import com.example.amazon.Entities.AmazonResponseCode;
import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Product;
import com.example.amazon.Repositories.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    public AmazonResponseEntity<?> createProduct(Product product){
        if(product.getName() == null || product.getPrice() == 0){
            return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_FIELD_IS_NULL);
        }
        productRepo.save(product);
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, product);
    }

    public void deleteProduct(long id){
        productRepo.deleteById(id);
    }

    public Optional<Product> checkProductById(long id){
        return productRepo.findById(id);
    }

    public AmazonResponseEntity<?> getById(long id){
        Optional<Product> product = productRepo.findById(id);
        if(product.isPresent()){
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, product);
        }
        return  new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_FOUND);
    }

    public AmazonResponseEntity<?> getAll(){
        List<Product> products = productRepo.findAll();
        if(products.isEmpty()){
            return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_FOUND);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, products);
    }

    public AmazonResponseEntity<?> updateProduct(Product product){
        if(product.getId() == 0){
            return new AmazonResponseEntity<>(AmazonResponseCode.ID_NOT_FOUND);
        }
        Optional<Product> product1 = productRepo.findById(product.getId());
        if(product1.isPresent()) {
            if (product.getName() == null) {                                     //check which attributes are updated
                product.setName(product1.get().getName());
            }
            if (product.getPrice() == 0) {
                product.setPrice(product1.get().getPrice());
            }
            if (product.getCategory() == null) {
                product.setCategory(product1.get().getCategory());
            }
            if (product.getDescription() == null) {
                product.setDescription(product1.get().getDescription());
            }
            if (product.getQuantity() == 0) {
                product.setQuantity(product1.get().getQuantity());
            }
            product1.get().setName(product.getName());                          //set product values
            product1.get().setPrice(product.getPrice());
            product1.get().setDescription(product.getDescription());
            product1.get().setQuantity(product.getQuantity());
            product1.get().setCategory(product.getCategory());
            productRepo.save(product1.get());
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, product1);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_FOUND);
    }
}
