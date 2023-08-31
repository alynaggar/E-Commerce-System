package com.example.amazon.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@IdClass(OrderProduct.OrderProductId.class)
public class OrderProduct {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    @Id
    private Order order;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    @Id
    private Product product;

    public OrderProduct() {
    }

    public OrderProduct(int quantity, double price, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Embeddable
    public static class OrderProductId implements Serializable {

        private long order;

        private long product;

        public OrderProductId() {
        }

        public OrderProductId(long order, long product) {
            this.order = order;
            this.product = product;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderProductId that = (OrderProductId) o;
            return order == that.order && product == that.product;
        }

        @Override
        public int hashCode() {
            return Objects.hash(order, product);
        }

        public long getOrder() {
            return order;
        }

        public void setOrder(long order) {
            this.order = order;
        }

        public long getProduct() {
            return product;
        }

        public void setProduct(long product) {
            this.product = product;
        }
    }
}
