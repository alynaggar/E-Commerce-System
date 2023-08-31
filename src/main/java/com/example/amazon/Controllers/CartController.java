package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Services.CartService;
import com.example.amazon.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private JwtService jwtService;

    public CartController(CartService cartService, JwtService jwtService) {
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getCart(@PathVariable long id) {
        return cartService.getById(id);
    }

    @GetMapping("/info")
    public AmazonResponseEntity<?> getCart(HttpServletRequest request) {
        return cartService.getByUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @GetMapping("")
    public AmazonResponseEntity<?> getAllCarts() {
        return cartService.getAll();
    }

    @PostMapping("")
    public AmazonResponseEntity<?> addProductToCart(@RequestParam int quantity, @RequestParam long productId, HttpServletRequest request) {
        return cartService.addProductToCart(quantity, productId, jwtService.extractUsernameFromRequestHeader(request));
    }

    @DeleteMapping("")
    public AmazonResponseEntity<?> deleteProductFromCart(HttpServletRequest request, @RequestParam long productId) {
        return cartService.deleteProductFromCart(jwtService.extractUsernameFromRequestHeader(request), productId);
    }

}
