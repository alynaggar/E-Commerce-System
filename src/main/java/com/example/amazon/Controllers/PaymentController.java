package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Services.JwtService;
import com.example.amazon.Services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentServices;
    private JwtService jwtService;


    public PaymentController(PaymentService paymentServices, JwtService jwtService) {
        this.paymentServices = paymentServices;
        this.jwtService = jwtService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getPayment(@PathVariable long id){
        return paymentServices.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/info")
    public AmazonResponseEntity<?> getUserPayment(HttpServletRequest request){
        return paymentServices.getByUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public AmazonResponseEntity<?> getAllPayments(){
        return paymentServices.getAll();
    }

}
