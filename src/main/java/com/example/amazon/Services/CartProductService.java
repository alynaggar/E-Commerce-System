package com.example.amazon.Services;

import com.example.amazon.Entities.Cart;
import com.example.amazon.Entities.CartProduct;
import com.example.amazon.Entities.CartProduct.CartProductId;
import com.example.amazon.Repositories.CartProductRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartProductService {

    private CartProductRepo cartProductRepo;

    public CartProductService(CartProductRepo cartProductRepo){
        this.cartProductRepo = cartProductRepo;
    }

    public void createCartProduct(CartProduct cartProduct){
        cartProductRepo.save(cartProduct);
    }

    public void deleteCartProduct(CartProductId cartProductId){
        cartProductRepo.deleteById(cartProductId);
    }

    public Optional<CartProduct> getById(CartProductId cartProductId){
        return cartProductRepo.findById(cartProductId);
    }

    public void deleteCartProductsByCart(Cart cart){
        cartProductRepo.deleteCartProductsByCart(cart);
    }
}
