package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Services.CartService;
import com.example.amazon.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getCart(@PathVariable long id) {
        return cartService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/info")
    public AmazonResponseEntity<?> getUserCart(HttpServletRequest request) {
        return cartService.getByUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public AmazonResponseEntity<?> getAllCarts() {
        return cartService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("")
    public AmazonResponseEntity<?> addProductToCart(@RequestParam int quantity, @RequestParam long productId, HttpServletRequest request) {
        return cartService.addProductToCart(quantity, productId, jwtService.extractUsernameFromRequestHeader(request));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("")
    public AmazonResponseEntity<?> deleteProductFromCart(HttpServletRequest request, @RequestParam long productId) {
        return cartService.deleteProductFromCart(jwtService.extractUsernameFromRequestHeader(request), productId);
    }

}
