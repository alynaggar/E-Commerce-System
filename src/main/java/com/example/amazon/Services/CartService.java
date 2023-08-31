package com.example.amazon.Services;

import com.example.amazon.Entities.*;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Repositories.CartRepo;
import com.example.amazon.Repositories.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepo cartRepo;
    private CartProductService cartProductService;
    private ProductService productService;
    private UserRepo userRepo;

    public CartService(CartRepo cartRepo, CartProductService cartProductService, ProductService productService, UserRepo userRepo) {
        this.cartRepo = cartRepo;
        this.cartProductService = cartProductService;
        this.productService = productService;
        this.userRepo = userRepo;
    }

    public void createCart(Cart cart){
        cartRepo.save(cart);
    }

    public void deleteCart(long id){
        cartRepo.deleteById(id);
    }

    public Optional<Cart> checkCartById(long id){
        return cartRepo.findById(id);
    }

    public AmazonResponseEntity<?> getById(long id){
        Optional<Cart> cart = cartRepo.findById(id);;
        if (cart.isPresent()) {
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, cart);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.CART_NOT_FOUND);
    }

    public AmazonResponseEntity<?> getByUser(String username){
        Optional<User> user = userRepo.findByUsername(username);
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, user.get().getCart());

    }

    public AmazonResponseEntity<?> getAll(){
        List<Cart> carts = cartRepo.findAll();
        if(carts.isEmpty()){
            return new AmazonResponseEntity<>(AmazonResponseCode.CART_NOT_FOUND);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, carts);
    }

    @Transactional
    public AmazonResponseEntity<?> addProductToCart(int quantity, long productId, String username) {
        Optional<Product> product = productService.checkProductById(productId);      //check that product exist
        Optional<User> user = userRepo.findByUsername(username);                     //check that user exist
        if (product.isPresent()) {
                if(quantity > product.get().getQuantity()){
                    return new AmazonResponseEntity<>(AmazonResponseCode.QUANTITY_MORE_THAN_AVAILABLE);
                }
                CartProduct cartProduct = new CartProduct(quantity, product.get().getPrice(), user.get().getCart(), product.get());
                product.get().setQuantity(product.get().getQuantity() - quantity);     //take quantity from product
                cartProductService.createCartProduct(cartProduct);
                productService.updateProduct(product.get());
                return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_FOUND);
    }

    @Transactional
    public AmazonResponseEntity<?> deleteProductFromCart(String username, long productId){
        Optional<Product> product = productService.checkProductById(productId);     //check that product exist
        Optional<User> user = userRepo.findByUsername(username);                    //check that user exist
        if(product.isPresent()) {
            Optional<CartProduct> cartProduct = cartProductService.getById(new CartProduct.CartProductId(user.get().getCart().getId(), productId));
            if (cartProduct.isPresent()) {
                product.get().setQuantity(product.get().getQuantity() + cartProduct.get().getQuantity());     //put quantity back to product
                cartProductService.deleteCartProduct(new CartProduct.CartProductId(user.get().getCart().getId(), productId));
                productService.updateProduct(product.get());
                return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS);
            }
            return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_IN_CART);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.PRODUCT_NOT_FOUND);
    }
}
