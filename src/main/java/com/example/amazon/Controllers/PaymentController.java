package com.example.amazon.Controllers;

import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Services.JwtService;
import com.example.amazon.Services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getPayment(@PathVariable long id){
        return paymentServices.getById(id);
    }

    @GetMapping("/info")
    public AmazonResponseEntity<?> getPayment(HttpServletRequest request){
        return paymentServices.getByUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @GetMapping("")
    public AmazonResponseEntity<?> getAllPayments(){
        return paymentServices.getAll();
    }

}
