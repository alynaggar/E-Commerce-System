package com.example.amazon.Repositories;

import com.example.amazon.Entities.Order;
import com.example.amazon.Entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepo extends JpaRepository<OrderProduct, OrderProduct.OrderProductId> {
    void deleteOrderProductsByOrder(Order order);
}
