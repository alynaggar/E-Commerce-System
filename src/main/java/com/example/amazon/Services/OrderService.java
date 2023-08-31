package com.example.amazon.Services;

import com.example.amazon.Entities.*;
import com.example.amazon.Entities.CartProduct.CartProductId;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Repositories.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepo orderRepo;
    private CartService cartService;
    private PaymentService paymentService;
    private CartProductService cartProductService;
    private OrderProductService orderProductService;
    private UserService userService;

    public OrderService(OrderRepo orderRepo, CartService cartService, PaymentService paymentService, CartProductService cartProductService, OrderProductService orderProductService, UserService userService) {
        this.orderRepo = orderRepo;
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.cartProductService = cartProductService;
        this.orderProductService = orderProductService;
        this.userService = userService;
    }

    public void createOrder(Order order){
        orderRepo.save(order);
    }

    @Transactional
    public void deleteOrder(long id){
        Optional<Order> order = orderRepo.findById(id);
        orderProductService.deleteOrderProductsByOrder(order.get());
        paymentService.deletePayment(order.get().getPayment().getId());
        orderRepo.deleteById(id);
    }

    public AmazonResponseEntity<?> getById(long id){
        Optional<Order> order = orderRepo.findById(id);
        if(order.isPresent()){
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, order);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.ORDER_NOT_FOUND);
    }

    public AmazonResponseEntity<?> getByUser(String username){
        Optional<User> user = userService.getByUsername(username);
        List<Order> orders = user.get().getOrders();
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, orders);
    }

    public AmazonResponseEntity<?> getAll(){
        List<Order> orders = orderRepo.findAll();
        if(orders.isEmpty()){
            return new AmazonResponseEntity<>(AmazonResponseCode.ORDER_NOT_FOUND);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, orders);
    }

    @Transactional
    public AmazonResponseEntity<?> confirmOrder(String username, Payment payment){
        Optional<User> user = userService.getByUsername(username);
            if(user.get().getCart().getPrice() == 0){
                return new AmazonResponseEntity<>(AmazonResponseCode.CART_IS_EMPTY);
            }
            Order order = new Order();
            order.setUser(user.get());                                //set user and date for order
            order.setDate(new Date());
            orderRepo.save(order);
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            for(int i = 0; i < user.get().getCart().getCartProducts().size(); i++){       //loop around cart products to delete products from cart and add them to order
                orderProduct.setProduct(user.get().getCart().getCartProducts().get(i).getProduct());
                orderProduct.setPrice(user.get().getCart().getCartProducts().get(i).getPrice());
                orderProduct.setQuantity(user.get().getCart().getCartProducts().get(i).getQuantity());
                orderProductService.createOrderProduct(orderProduct);
                CartProductId cartProductId = new CartProductId(user.get().getCart().getId(), user.get().getCart().getCartProducts().get(i).getProduct().getId());
            }
            cartProductService.deleteCartProductsByCart(user.get().getCart());            //empty user's cart
            payment.setOrder(order);                                            //add payment
            payment.setUser(order.getUser());
            payment.setAmount(user.get().getCart().getPrice());
            paymentService.createPayment(payment);
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, order);
    }
}
