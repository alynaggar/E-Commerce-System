package com.example.amazon.Entities;

import com.example.amazon.Entities.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProducts;
    @JsonIgnore
    @OneToOne(mappedBy = "cart", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    public Cart() {
    }

    public Cart(long id, double price, List<CartProduct> cartProducts, User user) {
        this.id = id;
        this.price = price;
        this.cartProducts = cartProducts;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }
}
