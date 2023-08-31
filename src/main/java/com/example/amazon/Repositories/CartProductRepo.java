package com.example.amazon.Repositories;

import com.example.amazon.Entities.Cart;
import com.example.amazon.Entities.CartProduct;
import com.example.amazon.Entities.CartProduct.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepo extends JpaRepository<CartProduct, CartProductId> {
    void deleteCartProductsByCart(Cart cart);
}
