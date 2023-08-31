package com.example.amazon.Services;

import com.example.amazon.Entities.Order;
import com.example.amazon.Entities.OrderProduct;
import com.example.amazon.Repositories.OrderProductRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderProductService {

    private OrderProductRepo orderProductRepo;

    public OrderProductService(OrderProductRepo orderProductRepo){
        this.orderProductRepo = orderProductRepo;
    }

    public void createOrderProduct(OrderProduct orderProduct){
        orderProductRepo.save(orderProduct);
    }

    public void deleteOrderProductsByOrder(Order order){
        orderProductRepo.deleteOrderProductsByOrder(order);
    }
}
