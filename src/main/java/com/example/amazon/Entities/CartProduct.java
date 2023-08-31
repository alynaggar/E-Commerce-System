package com.example.amazon.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
@IdClass(CartProduct.CartProductId.class)
public class CartProduct {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Id
    @JsonIgnore
    private Cart cart;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Id
    private Product product;

    public CartProduct() {
    }

    public CartProduct(int quantity, double price, Cart cart, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.cart = cart;
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Embeddable
    public static class CartProductId implements Serializable {

        private long cart;

        private long product;

        public CartProductId() {
        }

        public CartProductId(long cart, long product) {
            this.cart = cart;
            this.product = product;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartProductId that = (CartProductId) o;
            return cart == that.cart && product == that.product;
        }

        @Override
        public int hashCode() {
            return Objects.hash(cart, product);
        }

        public long getCart() {
            return cart;
        }

        public void setCart(long cartId) {
            this.cart = cartId;
        }

        public long getProduct() {
            return product;
        }

        public void setProduct(long productId) {
            this.product = productId;
        }
    }
}
