package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseCode;
import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Payment;
import com.example.amazon.Services.JwtService;
import com.example.amazon.Services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;
    private JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getOrder(@PathVariable long id){
        return orderService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/info")
    public AmazonResponseEntity<?> getUserOrder(HttpServletRequest request){
        return orderService.getByUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public AmazonResponseEntity<?> getAllOrders(){
        return orderService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("")
    public AmazonResponseEntity<?> confirmOrder(HttpServletRequest request, @RequestBody Payment payment){
        try {
            return orderService.confirmOrder(jwtService.extractUsernameFromRequestHeader(request), payment);
        }catch (DataIntegrityViolationException dive){
            return new AmazonResponseEntity<>(AmazonResponseCode.PAYMENT_FIELD_IS_NULL);
        }
    }
}
